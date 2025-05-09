"use client"

import { useEffect } from "react"
import { useRouter } from "next/navigation"
import ViewTransactions from "../components/view-transaction"
import { Sidebar } from "@/components/ui/sidebar"
import { useAuth } from "../components/auth/auth-provider"

export default function ViewTransactionPage() {
  const router = useRouter()
  const { token } = useAuth()

  useEffect(() => {
    if (!token) {
      router.push("/login")
    }
  }, [token, router])

  if (!token) {
    return null
  }

  return (
    <div className="bg-gray-200 min-h-screen flex">
      <Sidebar />
      <ViewTransactions />
    </div>
  )
}
