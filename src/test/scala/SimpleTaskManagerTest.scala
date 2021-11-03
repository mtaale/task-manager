import implementations.SimpleTaskManager
import models.{Priority, Process}
import org.scalatest.funsuite.AnyFunSuite

class SimpleTaskManagerTest extends AnyFunSuite {
  val taskManager = new SimpleTaskManager(2)

  test("add processes") {

    val process1 = Process("1", Priority.LOW)
    val process2 = Process("2", Priority.HIGH)
    val addResult1 = taskManager.add(process1)
    val addResult2 = taskManager.add(process2)

    assert(addResult1.isDefined)
    assert(addResult2.isDefined)
    assert(taskManager.list().length == 2)

    val process3 = Process("3", Priority.MEDIUM)
    val addResult3 = taskManager.add(process3)

    assert(addResult3.isEmpty)
    assert(taskManager.list().length == 2)
    assert(taskManager.list().head == process1)
    assert(taskManager.list().last == process2)

  }

  test("kill processes by pid") {
    taskManager.killAll

    taskManager.add(Process("1", Priority.LOW))
    taskManager.add(Process("2", Priority.HIGH))
    assert(taskManager.list().length == 2)

    taskManager.kill("1")
    assert(taskManager.list().length == 1)
    assert(!taskManager.list().exists(_.pid == "1"))

    taskManager.kill("2")
    assert(taskManager.list().isEmpty)
  }

  test("kill processes by group") {
    taskManager.killAll

    taskManager.add(Process("1", Priority.LOW))
    taskManager.add(Process("2", Priority.HIGH))
    assert(taskManager.list().length == 2)

    taskManager.killGroup(Priority.MEDIUM)
    assert(taskManager.list().length == 2)

    taskManager.killGroup(Priority.LOW)
    assert(!taskManager.list().exists(_.priority == Priority.LOW))
    assert(taskManager.list().nonEmpty)

    taskManager.killGroup(Priority.HIGH)
    assert(!taskManager.list().exists(_.priority == Priority.HIGH))
    assert(taskManager.list().isEmpty)

  }

  test("kill all processes") {
    taskManager.killAll

    taskManager.add(Process("1", Priority.LOW))
    taskManager.add(Process("2", Priority.HIGH))
    assert(taskManager.list().length == 2)

    taskManager.killAll
    assert(taskManager.list().isEmpty)
  }

}
