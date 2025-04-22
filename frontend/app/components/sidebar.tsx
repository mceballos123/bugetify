"use client"

import { useState } from "react"
import { useForm } from "react-hook-form"
import { useRouter } from "next/navigation"
import { registerUser } from "@/lib/actions"

export default function RegisterForm() {
  const [isSubmitting, setIsSubmitting] = useState(false)
  const router = useRouter()

  const {
    register,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm()

  const onSubmit = async (data: any) => {
    try {
      setIsSubmitting(true)
      await registerUser(data)
      router.push("/login")
    } catch (error: any) {
      setError("email", {
        type: "server",
        message: "This email is already taken. Please choose another.",
      })
      console.log("Email validation failed:", error.message)
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="bg-white w-96 min-h-96 rounded-2xl mx-auto mb-10 border border-gray-600">
      <div className="items-center justify-center flex mb-16">
        <h1 className="text-2xl font-serif mt-2 font-bold">Create a new account</h1>
      </div>

      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="form-control flex flex-col justify-center items-center space-y-6 mb-12 pt-1 p-5">
          {/* Full Name Input */}
          <div className="relative w-80">
            <input
              type="text"
              id="fullName"
              placeholder=" "
              className="peer w-full border border-gray-300 rounded-lg p-3 text-gray-900 focus:outline-none focus:ring-2 focus:ring-blue-500"
              {...register("name", {
                required: "Name is required",
                pattern: {
                  value: /^[A-Za-z]+(?:[-'\s][A-Za-z]+)*(?:\s(I{1,3}|II{1,2})|-\s?(I{1,3}|II{1,2}))?$/i,
                  message: "Name must be valid",
                },
              })}
            />
            <label
              htmlFor="fullName"
              className="absolute left-3 top-3 text-gray-500 transition-all peer-placeholder-shown:top-3 peer-placeholder-shown:text-gray-400 peer-placeholder-shown:text-base peer-focus:top-[-10px] peer-focus:text-sm peer-focus:text-blue-500 bg-white px-1"
            >
              Full Name
            </label>
          </div>
          {errors.name && <p className="text-red-500 text-sm">{errors.name.message as string}</p>}

          {/* Email Input */}
          <div className="relative w-80">
            <input
              type="email"
              id="email"
              placeholder=" "
              className="peer w-full border border-gray-300 rounded-lg p-3 text-gray-900 focus:outline-none focus:ring-2 focus:ring-blue-500"
              {...register("email", {
                required: "Email is required",
                pattern: {
                  value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
                  message: "Enter a valid email",
                },
              })}
            />
            <label
              htmlFor="email"
              className="absolute left-3 top-3 text-gray-500 transition-all peer-placeholder-shown:top-3 peer-placeholder-shown:text-gray-400 peer-placeholder-shown:text-base peer-focus:top-[-10px] peer-focus:text-sm peer-focus:text-blue-500 bg-white px-1"
            >
              Email
            </label>
          </div>
          {errors.email && <p className="text-red-500 text-sm">{errors.email.message as string}</p>}

          {/* Password Input */}
          <div className="relative w-80">
            <input
              type="password"
              id="password"
              placeholder=" "
              className="peer w-full border border-gray-300 rounded-lg p-3 text-gray-900 focus:outline-none focus:ring-2 focus:ring-blue-500"
              {...register("password", {
                required: "Password is required",
                minLength: {
                  value: 6,
                  message: "Password must be at least 6 characters",
                },
              })}
            />
            <label
              htmlFor="password"
              className="absolute left-3 top-3 text-gray-500 transition-all peer-placeholder-shown:top-3 peer-placeholder-shown:text-gray-400 peer-placeholder-shown:text-base peer-focus:top-[-10px] peer-focus:text-sm peer-focus:text-blue-500 bg-white px-1"
            >
              Password
            </label>
          </div>
          {errors.password && <p className="text-red-500 text-sm">{errors.password.message as string}</p>}

          {/* Submit Button */}
          <button
            type="submit"
            className="w-80 bg-blue-500 text-white rounded-lg p-3 hover:bg-blue-600 transition"
            disabled={isSubmitting}
          >
            {isSubmitting ? "Registering..." : "Register"}
          </button>
        </div>
      </form>
    </div>
  )
}
