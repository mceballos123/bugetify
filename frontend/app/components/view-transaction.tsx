"use client"

import { useState, useEffect } from "react"
import { getAllTransactions } from "@/lib/actions"
import TransactionDetails from "./transaction-detail"

export default function ViewTransactions() {
  const [transactions, setTransaction] = useState<any[]>([])
  const [displayDetails, setDisplayDetails] = useState(false)
  const [transactionId, setTransactionId] = useState("")
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const fetchTransactions = async () => {
      try {
        setIsLoading(true)
        const result = await getAllTransactions()
        setTransaction(result.data)
      } catch (error) {
        console.error("Failed to fetch transactions:", error)
      } finally {
        setIsLoading(false)
      }
    }

    fetchTransactions()
  }, [])

  if (isLoading) {
    return (
      <div className="ml-[300px] mt-10">
        <h1 className="font-serif text-3xl font-bold">Transaction List</h1>
        <p className="mt-5 text-gray-500">Loading transactions...</p>
      </div>
    )
  }

  return (
    <div>
      <div className="ml-[300px]">
        <h1 className="font-serif text-3xl font-bold">Transaction List</h1>
        <div className="overflow-visible">
          {transactions.length === 0 ? (
            <p className="mt-5 text-gray-500">No transactions found. Add some transactions to get started.</p>
          ) : (
            transactions.map((transaction) => (
              <div
                className="flex flex-row border border-gray-300 w-[790px] h-[49px] mb-4 rounded-md shadow-md bg-white cursor-pointer hover:bg-gray-50"
                key={transaction._id}
                onClick={() => {
                  setDisplayDetails(true)
                  setTransactionId(transaction._id)
                }}
              >
                <p className="font-serif ml-2 mt-2">{transaction.paymentDescription}</p>
                <p className="text-green-500 font-serif tabular-nums text-right mt-2 ml-auto mr-4">
                  &#36;{transaction.amount}
                </p>
              </div>
            ))
          )}
        </div>
      </div>

      <div>
        <TransactionDetails
          onOpen={displayDetails}
          onClose={() => setDisplayDetails(false)}
          itemId={transactionId}
          setTransaction={setTransaction}
        />
      </div>
    </div>
  )
}
