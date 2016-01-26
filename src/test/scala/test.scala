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
    val startTime = System.currentTimeMillis
    val socket = new Socket("localhost", 2020)
    val outputStream = new DataOutputStream(socket.getOutputStream())

    (1 to packetsNumber).foreach {
      (i: Int) => {
        writeHeader(outputStream, length)
        outputStream.write(Array.fill(length){0.toByte})
      }
    }
    socket.close();
    val totalTime = System.currentTimeMillis - startTime
    println("done writing packets in " + totalTime / 1000.0  + " seconds ")
  }
}

def test(packets: Int, length: Int) {
  (new Thread(new TestTask(packets, length))).start()
}

(1 to 8).foreach { i =>  test(500000, 32) }
