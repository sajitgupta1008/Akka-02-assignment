import Models.Biller

val biller = Biller("sa","fg",12,"df",1000,0,0,0)


biller.copy(executedIterations = biller.executedIterations + 1, totalIterations =
  biller.totalIterations + 1, paidAmount = biller.paidAmount + biller.amount, amount = 0)