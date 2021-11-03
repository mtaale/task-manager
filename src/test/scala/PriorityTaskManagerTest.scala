import implementations.PriorityTaskManager
import models.{Priority, Process}
import org.scalatest.funsuite.AnyFunSuite

class PriorityTaskManagerTest extends AnyFunSuite {
  val taskManager = new PriorityTaskManager(3)

  test("add processes") {

    // add process to make the task manager full
    val process1 = Process("1", Priority.MEDIUM)
    val process2 = Process("2", Priority.LOW)
    val process3 = Process("3", Priority.MEDIUM)
    val addResult1 = taskManager.add(process1)
    val addResult2 = taskManager.add(process2)
    val addResult3 = taskManager.add(process3)

    assert(addResult1.isDefined)
    assert(addResult2.isDefined)
    assert(addResult3.isDefined)
    assert(taskManager.list().length == 3)

    // add a process which should kill the lower priority and oldest process
    val process4 = Process("4", Priority.MEDIUM)
    val addResult4 = taskManager.add(process4)

    assert(addResult4.isDefined)
    assert(taskManager.list().length == 3)
    assert(!taskManager.list().exists(_.pid == "2"))
    assert(taskManager.list().exists(_.pid == "1"))

    // add a process which should not be accepted since there is no lower priority in the task manger
    val process5 = Process("5", Priority.MEDIUM)
    val addResult5 = taskManager.add(process5)
    assert(addResult5.isEmpty)

    // add a process with a higher priority which should be accepted
    val process6 = Process("6", Priority.HIGH)
    val addResult6 = taskManager.add(process6)

    assert(addResult6.isDefined)
    assert(taskManager.list().length == 3)
    assert(!taskManager.list().exists(_.pid == "1"))
    assert(taskManager.list().exists(_.pid == "6"))

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

    taskManager.killGroup(2)
    assert(taskManager.list().length == 2)

    taskManager.killGroup(1)
    assert(!taskManager.list().exists(_.priority == Priority.LOW))
    assert(taskManager.list().nonEmpty)

    taskManager.killGroup(3)
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
