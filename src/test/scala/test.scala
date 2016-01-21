import java.net.Socket
import java.io.DataOutputStream
import java.util.concurrent.{Executors, ExecutorService}

val rand = scala.util.Random

// 32 bytes
val lightPacket = Map[String, Int] (
  "type" -> 0,
  "version" -> 2,
  "length" -> 12,
  "status" -> 0,
  "action" -> 1,
  "jobid" -> 1
)

// 1024 bytes
val heavyPacket = Map[String, Int] (
  "type" -> 0,
  "version" -> 2,
  "length" -> 1004,
  "status" -> 0,
  "action" -> 1,
  "jobid" -> 1
)

def writePacket(stream: DataOutputStream, packet: Map[String, Int]) {
  stream.writeByte(packet("type").toByte)
  stream.writeByte(packet("version").toByte)
  stream.writeInt(packet("length"))
  stream.writeShort(packet("status").toShort)
  stream.writeInt(packet("action"))
  stream.writeLong(packet("jobid").toLong)
}

class TestTask(packetsNumber: Int, packet: Map[String, Int]) extends Runnable {
  def run() {
    val socket = new Socket("localhost", 2020)
    val outputStream = new DataOutputStream(socket.getOutputStream())

    (1 to packetsNumber).foreach {
      (i: Int) => {
        writePacket(outputStream, packet)
        outputStream.write(Array.fill(packet("length")){rand.nextInt.toByte})
      }
    }
    socket.close();
  }
}

val pool: ExecutorService = Executors.newFixedThreadPool(5)
try {
  pool.execute(new TestTask(10000, lightPacket))
  pool.execute(new TestTask(10000, lightPacket))
  pool.execute(new TestTask(10000, lightPacket))
  pool.execute(new TestTask(10000, lightPacket))
  pool.execute(new TestTask(10000, lightPacket))
} finally {
  pool.shutdown()
}
