package Models

case class Biller(category: String, name: String, accNo: Int, transactionDate: String, amount: Int,
                  totalIterations: Int, executedIterations: Int, paidAmount: Int)
