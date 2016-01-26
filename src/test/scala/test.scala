import java.net.Socket
import java.io.DataOutputStream
import java.util.concurrent.{Executors, ExecutorService}

def writeHeader(stream: DataOutputStream, length: Int) {
  try {
    stream.writeByte(0)
    stream.writeByte(1)
    stream.writeInt(length)
    stream.writeShort(0)
    stream.writeInt(1)
    stream.writeLong(0)
  } catch {
    case e: Error =>
    println(e)
  }
}

class TestTask(packetsNumber: Int, length: Int) extends Runnable {
  def run() {
    val socket = new Socket("localhost", 2020)
    val outputStream = new DataOutputStream(socket.getOutputStream())

    val startTime = System.nanoTime
    (1 to packetsNumber).foreach {
      (i: Int) => {
        writeHeader(outputStream, length)
        outputStream.write(Array.fill(length){0.toByte})
      }
    }
    socket.close();
    println("done writing packets in " + (System.nanoTime - startTime) / 10e9 + " seconds ")
  }
}

val pool: ExecutorService = Executors.newFixedThreadPool(5)
try {
  pool.execute(new TestTask(1000000, 32))
  pool.execute(new TestTask(1000000, 32))
  pool.execute(new TestTask(1000000, 32))
  pool.execute(new TestTask(1000000, 32))
  pool.execute(new TestTask(1000000, 32))
  pool.execute(new TestTask(1000000, 32))
  pool.execute(new TestTask(1000000, 32))
  pool.execute(new TestTask(1000000, 32))
} finally {
  pool.shutdown()
}
