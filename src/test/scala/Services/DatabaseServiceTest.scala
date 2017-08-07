package Services

import Models.{Account, Biller}
import org.scalatest.FunSuite
import org.scalatest.mockito.MockitoSugar

import scala.collection.mutable

class DatabaseServiceTest extends FunSuite with MockitoSugar {

  val acNo = 1
  val name = "sajit"
  val address = "lko"
  val userName = "sajit123"
  val amount = 1000
  val account: Account = Account(acNo, name, address, userName, amount)

  val biller: List[Biller] = List(Biller(name, name, acNo, name, amount, acNo, acNo, amount))
  val database = new DatabaseService()

  import database._

  test("testAddAcount") {
    assert(addAccount(account) == mutable.Map("sajit123" -> account))
  }

  test("testGetAccounts") {
    assert(getAccounts == mutable.Map(userName -> account))

  }

  test("testAddBiller") {
    assert(addBiller(acNo, biller) == mutable.Map(acNo -> biller))

  }

  test("testGetBillers") {
    assert(getBillers == mutable.Map(acNo -> biller))
  }

  test("testContainsAccount") {
    assert(containsAccount(userName))
  }


  test("testGetAccount") {
    assert(getAccount(account.accountNo) == account)
  }

  test("testUpdateBalance") {
    assert(updateBalance(account.accountNo, amount * 2) == mutable.Map(userName -> account.copy(initialAmount = amount * 2)))
  }

  test("testGetAccountNo") {
    assert(getAccountNo(userName) == acNo)
  }
  test("testUpdateBiller") {
    assert(updateBiller(acNo, biller.head.copy(totalIterations = 1))==List(biller.head.copy(totalIterations = 1)))
  }

}
