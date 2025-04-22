"use client"

import type React from "react"

import { useEffect, useState, useRef } from "react"
import { useRouter } from "next/navigation"
import flatpickr from "flatpickr"
import "flatpickr/dist/flatpickr.min.css"
import { createTransaction } from "../lib/actions"

export default function TransactionForm() {
  const router = useRouter()
  const [hovered, setHovered] = useState(false)
  const [clicked, setClicked] = useState(false)
  const [isFocused, setIsFocused] = useState(false)
  const [recipientInfoFocused, setRecipientInfoFocused] = useState(false)
  const [isSubmitting, setIsSubmitting] = useState(false)

  const [transaction, setTransactionInput] = useState({
    amount: 1,
    transactionStatus: "pending",
    paymentDescription: "",
    transactionCategory: "Personal Expenses",
    transactionDate: "",
    paymentMethod: "Debit Card",
    recipientInfo: "",
  })

  const datepickerRef = useRef<HTMLInputElement>(null)

  useEffect(() => {
    if (datepickerRef.current) {
      flatpickr(datepickerRef.current, {
        dateFormat: "Y-m-d",
        allowInput: true,
        onChange: (selectedDates, dateStr) => {
          setTransactionInput((prev) => ({ ...prev, transactionDate: dateStr }))
        },
      })
    }
  }, [])

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault()

    try {
      setIsSubmitting(true)
      await createTransaction(transaction)

      // Reset form after successful submission
      setTransactionInput({
        amount: 1,
        transactionStatus: "pending",
        paymentDescription: "",
        transactionCategory: "Personal Expenses",
        transactionDate: "",
        paymentMethod: "Debit Card",
        recipientInfo: "",
      })

      // Show success message or redirect
      alert("Transaction added successfully!")
    } catch (error: any) {
      console.error("Transaction submission error:", error.message)
      alert("Failed to add transaction. Please try again.")
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="bg-white rounded-2xl border border-solid w-[650px] h-[720px] flex ml-[600px] mt-[50px]">
      <form onSubmit={handleSubmit}>
        <div className="mx-auto">
          <div className="flex flex-col">
            <label className="block mt-3 mb-2 text-sm font-medium text-gray-900 ml-[100px] font-serif">Price</label>

            <input
              onChange={(event) => setTransactionInput({ ...transaction, amount: Number(event.target.value) })}
              value={transaction.amount}
              className="ml-3 flex h-8 w-[300px] rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium file:text-foreground placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 shadow-md font-serif"
              min="1"
              type="number"
              step=".01"
              placeholder="Enter amount"
              required
            />
          </div>

          <div className="flex flex-col gap-3 items-center justify-center">
            <label htmlFor="status" className="mt-5 font-serif block mb-2 text-sm font-medium text-gray-900 rounded-lg">
              Select transaction status
            </label>
            <select
              onChange={(event) => {
                setTransactionInput((prev) => ({ ...prev, transactionStatus: event.target.value }))
              }}
              value={transaction.transactionStatus}
              className="ml-3 flex h-10 w-full items-center justify-between rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 [&>span]:line-clamp-1 shadow-md font-serif"
              id="status"
              required
            >
              <option value="completed">completed</option>
              <option value="pending">pending</option>
              <option value="failed">failed</option>
            </select>
          </div>

          <div className="flex flex-col gap-3 items-center justify-center">
            <label className="mt-5 font-serif block mb-2 text-sm font-medium text-gray-900 mr-6 mt-1">Name</label>
            <input
              onChange={(event) => {
                setTransactionInput({ ...transaction, paymentDescription: event.target.value })
              }}
              value={transaction.paymentDescription}
              className={`w-full ml-4 max-w-xs h-10 p-3 rounded-lg border-2 border-light-gray outline-none transition-all duration-300 ease-in-out shadow-lg text-sm font-serif ${
                isFocused ? "border-gray-400" : "border-light-gray"
              }`}
              type="text"
              placeholder="Enter transaction"
              onFocus={() => setIsFocused(true)}
              onBlur={() => setIsFocused(false)}
              required
            />
          </div>

          <div className="mt-[15px]">
            <label className="text-sm font-serif text-gray-900 ml-[45px]">Purchase Date</label>
            <input
              ref={datepickerRef}
              className="border-2 border-gray-100 rounded px-3 py-2 w-full h-10 shadow-md text-sm font-serif mt-[15px] ml-[5px]"
              type="text"
              placeholder="Select a date"
              value={transaction.transactionDate}
              readOnly
            />
          </div>

          <div className="flex flex-col gap-3 items-center justify-center">
            <label className="mt-4 font-serif block mb-2 text-sm font-medium text-gray-900 mr-9">
              Payment Category
            </label>

            <label htmlFor="transactionCategory" className="sr-only">Transaction Category</label>
            <select
              id="transactionCategory"
              onChange={(event) => {
                setTransactionInput((prev) => ({ ...prev, transactionCategory: event.target.value }))
              }}
              value={transaction.transactionCategory}
              className="ml-3 flex h-10 w-full items-center justify-between rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 [&>span]:line-clamp-1shadow-md font-serif"
              required
            >
              <option value="Personal Expenses">Personal Expenses</option>
              <option value="Food">Food</option>
              <option value="Entertainment">Entertainment</option>
              <option value="Utilities">Utilities</option>
              <option value="Other">Other</option>
            </select>
          </div>

          <div className="flex flex-col gap-3 items-center justify-center">
            <label className="mt-5 font-serif block mb-2 text-sm font-medium text-gray-900 mr-9">Payment Method</label>
            <select id="status"
              onChange={(event) => {
                setTransactionInput({ ...transaction, paymentMethod: event.target.value })
              }}
              value={transaction.paymentMethod}
              className="ml-3 flex h-10 w-full items-center justify-between rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 [&>span]:line-clamp-1 shadow-md font-serif"
              required
            >
              <option value="Debit Card">Debit Card</option>
              <option value="Cash">Cash</option>
              <option value="Credit Card">Credit Card</option>
              <option value="Digital Wallet">Digital Wallet</option>
              <option value="Other">Other</option>
            </select>
          </div>

          <div className="flex flex-col gap-3 items-center justify-center">
            <label className="font-serif block mb-2 text-sm font-medium text-gray-900 mr-[75px] mt-2">Recipient</label>
            <input
              onChange={(event) => setTransactionInput({ ...transaction, recipientInfo: event.target.value })}
              value={transaction.recipientInfo}
              className={`w-full ml-4 max-w-xs h-10 p-3 rounded-lg border-2 border-light-gray outline-none transition-all duration-300 ease-in-out shadow-lg text-sm font-serif ${
                recipientInfoFocused ? "border-gray-400" : "border-light-gray"
              }`}
              type="text"
              placeholder="Enter Recipient"
              onFocus={() => setRecipientInfoFocused(true)}
              onBlur={() => setRecipientInfoFocused(false)}
              required
            />
          </div>

          <button
            type="submit"
            className={`ml-[150px] mt-[17px] relative w-[150px] h-[40px] cursor-pointer flex items-center border border-[#4a90e2] bg-[#3aa856] 
                        ${hovered ? "bg-[#34974d]" : ""} 
                        ${clicked ? "border-[#2e8644]" : ""}`}
            onMouseEnter={() => setHovered(true)}
            onMouseLeave={() => setHovered(false)}
            onMouseDown={() => setClicked(true)}
            onMouseUp={() => setClicked(false)}
            disabled={isSubmitting}
          >
            <span
              className={`transition-all duration-200 transform ${hovered ? "text-transparent" : "text-white"} font-semibold ml-[30px]`}
            >
              {isSubmitting ? "Adding..." : "Add Item"}
            </span>
            <span
              className={`absolute transition-all duration-400 transform ${hovered ? "w-[148px] translate-x-0" : "w-[39px] translate-x-[109px]"} 
                        bg-[#34974d] flex items-center justify-center`}
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="48"
                viewBox="0 0 24 24"
                strokeWidth="2"
                strokeLinejoin="round"
                strokeLinecap="round"
                stroke="currentColor"
                height="38"
                fill="none"
                className="w-[30px] stroke-white"
              >
                <line y2="19" y1="5" x2="12" x1="12"></line>
                <line y2="12" y1="12" x2="19" x1="5"></line>
              </svg>
            </span>
          </button>
        </div>
      </form>
    </div>
  )
}
