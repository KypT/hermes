package com.etagi.crm;

import java.net.ServerSocket
import java.util.concurrent.{Executors, ExecutorService}

class TcpEndpoint(port: Int, poolSize: Int) extends Runnable {
  val serverSocket = new ServerSocket(port)
  val pool: ExecutorService = Executors.newFixedThreadPool(poolSize)

  def run() {
    try {
      while (true) {
        val socket = serverSocket.accept()
        pool.execute(new TcpConnectionHandler(socket))
      }
    } finally {
      pool.shutdown()
    }
  }
}
