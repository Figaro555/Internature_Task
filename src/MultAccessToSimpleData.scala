import scala.io.StdIn
import scala.util.Random

object Main extends App {

  println("enter thread numbers");
  val threadsNum: Int = StdIn.readInt();
  var buffer: Array[Int] = Array(1);
  for (i <- 0 to threadsNum) (new Thread(new AccessThread(buffer, i))).start();
}


class AccessThread(var buffer: Array[Int], num: Int) extends Runnable {
  override def run(): Unit = {
    while (true) {

      buffer.synchronized {
        buffer(0) = if (num % 2 == 0) 0 else buffer(0) + 1
        println(buffer(0) + " " + num);
      }

      Thread.sleep((new Random).nextInt(2000))
    }

  }
}