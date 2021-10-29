import Cache.cache
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import java.lang.Thread.interrupted
import scala.collection.mutable
import scala.util.Random


object Cache {
  val cache: mutable.Map[String, Byte] = new mutable.HashMap[String, Byte];
}

object Main1 extends App {

  cache.put("name", 0)
  cache.put("photos", 0)
  cache.put("users", 0)
  cache.put("friends", 0)
  cache.put("massages", 0)

  val editActorsAmount = 5
  val system = ActorSystem("MySystem")

  for (i <- 0 to editActorsAmount) (new Thread(new MassageEmulator(system.actorOf(Props(new CacheEditor1()), "MyActor" + i)))).start();
}

class CacheEditor1() extends Actor {

  override def receive = {
    case key: String => {

      var oldValue: Byte = (cache.get(key)).getOrElse(0)
      var newValue: Byte = (oldValue + 1).toByte
      do {
        oldValue = (cache.get(key)).getOrElse(0)
        newValue = (oldValue + 1).toByte

      } while (oldValue != cache.get(key).getOrElse(0))

      cache.put(key, newValue)
      println(s"putting in $key value $newValue")
    }
    case _ => println("message can not be received")
  }
}


class MassageEmulator(sendTo: ActorRef) extends Runnable {
  override def run(): Unit = {

    val values: List[String] = cache.keySet.toList

    val random: Random = new Random();
    val actorName: String = sendTo.path.name

    while (!interrupted()) {
      sendTo ! values(random.nextInt(values.length))

      println(s"sending changing to $actorName")

      Thread.sleep(random.nextInt(1000) + 500)

    }

  }
}

