"use client"

import type React from "react"

import { useState, useEffect } from "react"
import { getTransaction, deleteTransactionById, getAllTransactions } from "@/lib/actions"
import { Dialog } from "@/components/ui/dialog"

interface TransactionDetailsProps {
  onOpen: boolean
  onClose: () => void
  itemId: string
  setTransaction: React.Dispatch<React.SetStateAction<any[]>>
}

export default function TransactionDetails({ onOpen, onClose, itemId, setTransaction }: TransactionDetailsProps) {
  const [transactionDetail, setTransactionDetails] = useState<any>(null)
  const [isLoading, setIsLoading] = useState(false)
  const [isDeleting, setIsDeleting] = useState(false)

  useEffect(() => {
    if (itemId && onOpen) {
      const fetchTransactionDetail = async () => {
        try {
          setIsLoading(true)
          const response = await getTransaction(itemId)
          setTransactionDetails(response.data)
        } catch (error) {
          console.error("Failed to fetch transaction details:", error)
        } finally {
          setIsLoading(false)
        }
      }

      fetchTransactionDetail()
    }
  }, [itemId, onOpen])

  const deleteButton = async () => {
    try {
      setIsDeleting(true)
      await deleteTransactionById(transactionDetail._id)
      onClose()

      // Re-fetch transactions after deletion
      const result = await getAllTransactions()
      setTransaction(result.data)
    } catch (error) {
      console.error("Failed to delete transaction:", error)
    } finally {
      setIsDeleting(false)
    }
  }

  if (!onOpen) return null

  return (
    <Dialog open={onOpen} onOpenChange={onClose}>
      <div className="fixed inset-0 bg-gray-500 bg-opacity-75 z-10 flex items-center justify-center">
        {isLoading ? (
          <div className="bg-white p-6 rounded-lg">
            <p>Loading transaction details...</p>
          </div>
        ) : transactionDetail ? (
          <div className="border bg-white w-[590px] h-[299px] rounded-3xl shadow-2xl mx-auto">
            <div className="ml-5 mt-4">
              <div className="flex flex-row">
                <h1 className="font-serif font-bold">{transactionDetail.paymentDescription}</h1>
                <button onClick={onClose} className="ml-[380px] text-gray-300">
                  X
                </button>
              </div>
              <p className="font-serif text-gray-400">Transaction Details</p>
            </div>

            <div className="flex flex-col mt-6 ml-5">
              <p className="font-serif">
                <span className="font-bold">Amount:</span> ${transactionDetail.amount}
              </p>
              <p className="font-serif">
                <span className="font-bold font-serif">Recipient Name:</span> {transactionDetail.recipientInfo}
              </p>
              <p className="font-serif">
                <span className="font-bold font-serif">Category:</span> {transactionDetail.transactionCategory}
              </p>
              <p className="font-serif">
                <span className="font-bold font-serif">Date:</span> {transactionDetail.transactionDate}
              </p>
              <p className="font-serif">
                <span className="font-bold font-serif">Payment:</span> {transactionDetail.paymentMethod}
              </p>
              <p className="font-serif">
                <span className="font-bold font-serif">Status:</span> {transactionDetail.transactionStatus}
              </p>
            </div>

            <div className="flex flex-row gap-5 ml-[360px] mt-4">
              <button
                className="rounded text-white bg-black font-bold py-2 px-4 font-serif"
                onClick={() => {
                  // For now, we'll just close the modal
                  // In a real app, you'd implement the edit functionality
                  onClose()
                }}
              >
                Update
              </button>
              <button
                onClick={deleteButton}
                className="rounded text-white bg-red-500 font-bold py-2 px-4 font-serif"
                disabled={isDeleting}
              >
                {isDeleting ? "Deleting..." : "Delete"}
              </button>
            </div>
          </div>
        ) : (
          <div className="bg-white p-6 rounded-lg">
            <p>Transaction not found</p>
          </div>
        )}
      </div>
    </Dialog>
  )
}
