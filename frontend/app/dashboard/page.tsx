"use client"

import { useEffect } from "react"
import { useRouter } from "next/navigation"
import DashboardForm from "@/components/dashboard-form"
import SideBar from "@/components/sidebar/SideBar"
import { useAuth } from "@/components/auth/auth-provider"

export default function DashboardPage() {
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
    <div className="min-h-screen bg-gray-200 flex">
      <SideBar />
      <DashboardForm />
    </div>
  )
}
