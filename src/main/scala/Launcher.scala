import com.etagi.crm._

object Launcher {
  def main(args: Array[String]): Unit = {
    (new TcpEndpoint(2020, 1)).run
  }
}
