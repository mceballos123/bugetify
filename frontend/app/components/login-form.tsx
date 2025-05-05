"use client"

import { useState } from "react"
import { useForm } from "react-hook-form"
import { useRouter } from "next/navigation"
//import { loginUser } from "@/lib/actions"
import { loginUser } from "../lib/actions"
//import { useAuth } from "@/components/auth/auth-provider"
import { useAuth } from "./auth/auth-provider"

export default function LoginForm() {
  const [isSubmitting, setIsSubmitting] = useState(false)
  const router = useRouter()
  const { setToken } = useAuth()

  const {
    register,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm()

  const onSubmit = async (data: any) => {
    try {
      setIsSubmitting(true)
      const response = await loginUser(data)

      if (response && response.tokenKey) {
        setToken(response.tokenKey)
        router.push("/dashboard")
      } else {
        setToken(null)
        throw new Error("Invalid response from server")
      }
    } catch (error: any) {
      setError("email", {
        type: "server",
        message: "Password or Email is incorrect",
      })
      setError("password", {
        type: "server",
        message: "Password or Email is incorrect",
      })

      setToken(null)
      console.error("Login error:", error.message)
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="bg-white w-96 min-h-96 rounded-2xl mx-auto mb-10 border border-gray-600">
      <div className="items-center justify-center flex mb-16">
        <h1 className="text-2xl font-serif mt-2 font-bold">Login</h1>
      </div>

      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="flex flex-col justify-center items-center space-y-6 mb-12 pt-1 p-6">
          {/* Email Input with Floating Label */}
          <div className="relative w-80">
            <input
              type="email"
              id="email"
              className="peer w-full border border-gray-300 mt-3 rounded-lg pl-3 pt-4 pb-2 text-m font-serif focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder=" "
              {...register("email", {
                required: { value: true, message: "Email is required" },
                validate: {
                  isValidEmail: (value) =>
                    /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value) || "Email is invalid",
                },
              })}
            />
            <label
              htmlFor="email"
              className="absolute left-3 top-3 text-gray-500 text-m font-serif peer-placeholder-shown:top-6 peer-placeholder-shown:text-lg peer-placeholder-shown:text-gray-400 peer-focus:top-3 peer-focus:text-blue-500 peer-focus:text-sm transition-all"
            >
              Email
            </label>
          </div>
          {errors.email && <p className="text-sm text-red-500">{errors.email.message as string}</p>}

          {/* Password Input with Floating Label */}
          <div className="relative w-80">
            <input
              type="password"
              id="password"
              className="peer w-full border border-gray-300 rounded-lg pl-3 pt-5 pb-2 mt-2 text-m font-serif focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder=" "
              {...register("password", {
                required: { value: true, message: "Password is required" },
                validate: {
                  isValidPassword: (value) =>
                    /^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[A-Z])[a-zA-Z0-9!@#$%^&*]{6,16}$/.test(value) ||
                    "Password is invalid",
                },
              })}
            />
            <label
              htmlFor="password"
              className="absolute left-3 top-3 text-gray-500 text-m font-serif peer-placeholder-shown:top-6 peer-placeholder-shown:text-lg peer-placeholder-shown:text-gray-400 peer-focus:top-3 peer-focus:text-blue-500 peer-focus:text-sm transition-all"
            >
              Password
            </label>
          </div>
          {errors.password && <p className="text-sm text-red-500">{errors.password.message as string}</p>}

          {/* Submit Button */}
          <div className="pt-16">
            <button
              className="w-80 bg-blue-500 text-white rounded-lg p-3 hover:bg-blue-600 transition"
              type="submit"
              disabled={isSubmitting}
            >
              {isSubmitting ? "Logging in..." : "Submit"}
            </button>
          </div>
        </div>
      </form>
    </div>
  )
}
