import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.collection.mutable
import scala.util.Random
import scala.util.control.Breaks.break

object Main1 extends App {
  var cache: mutable.Map[String, Byte] = new mutable.HashMap[String, Byte];
  cache.put("name", 0)
  cache.put("photos", 0)
  cache.put("users", 0)
  cache.put("friends", 0)
  cache.put("massages", 0)

  val editActorsAmount = 5
  val system = ActorSystem("MySystem")

  var actor: ActorRef = system.actorOf(Props(new CacheEditor1(cache)), "MyActor")

  for (i <- (0 to editActorsAmount)) (new Thread(new MassageEmulator(system.actorOf(Props(new CacheEditor1(cache)), "MyActor" + i), cache.keySet.toList))).start();
}

class CacheEditor1(var cache: mutable.Map[String, Byte]) extends Actor {

  override def receive = {
    case key: String => {
      try{
        val oldValue: Byte = (cache.get(key)).getOrElse(0)
        while (true) {

          val newValue: Byte = (oldValue + 1).toByte


          if (oldValue == cache.get(key).getOrElse(0)) {
            cache.put(key, newValue)
            println(s"putting in $key value $newValue")
            break;
          }

        }
      }
      catch{
        case _ => println("Something exactly wrong")

      }



    }
    case _ => println("something wrong")
  }
}


class MassageEmulator(sendTo: ActorRef, val values: List[String]) extends Runnable {
  override def run(): Unit = {

    val random: Random = new Random();
    val actorName: String = sendTo.path.name

    while (true) {
      sendTo ! values(random.nextInt(values.length))

      println(s"sending changing to $actorName")
      Thread.sleep(random.nextInt(1000) + 500)

    }

  }
}

