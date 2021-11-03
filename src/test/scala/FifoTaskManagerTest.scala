
import implementations.FifoTaskManager
import models.{Priority, Process, Sort}
import org.scalatest.funsuite.AnyFunSuite

class FifoTaskManagerTest extends AnyFunSuite {
  val taskManager = new FifoTaskManager(3)

  test("add processes") {

    // add process to make the task manager full
    val process1 = Process("1", Priority.LOW)
    val process2 = Process("2", Priority.HIGH)
    val process3 = Process("3", Priority.MEDIUM)
    val addResult1 = taskManager.add(process1)
    val addResult2 = taskManager.add(process2)
    val addResult3 = taskManager.add(process3)

    assert(addResult1.isDefined)
    assert(addResult2.isDefined)
    assert(addResult3.isDefined)
    assert(taskManager.list().length == 3)

    // add a process which should result in removing a process from the queue front
    val process4 = Process("4", Priority.MEDIUM)
    val addResult4 = taskManager.add(process4)

    assert(addResult4.isDefined)
    assert(taskManager.list().length == 3)
    assert(taskManager.list().head == process2)
    assert(taskManager.list().last == process4)

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

  test("sort processes") {
    taskManager.killAll
    taskManager.add(Process("1", Priority.LOW))
    taskManager.add(Process("2", Priority.HIGH))
    taskManager.add(Process("3", Priority.MEDIUM))

    val orderedProcessesAsc = taskManager.list(Some(Sort.PRIORITY_ASC))
    assert(orderedProcessesAsc.head.priority == Priority.LOW)
    assert(orderedProcessesAsc.last.priority == Priority.HIGH)

    val orderedProcessesDesc = taskManager.list(Some(Sort.PRIORITY_DESC))
    assert(orderedProcessesDesc.head.priority == Priority.HIGH)
    assert(orderedProcessesDesc.last.priority == Priority.LOW)
  }

}
