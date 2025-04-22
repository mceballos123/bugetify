import RegisterForm from "@/components/register-form"
import Link from "next/link"
import Image from "next/image"

export default function RegisterPage() {
  return (
    <div className="bg-white h-full w-full absolute">
      <div className="flex ml-10 flex-col">
        <div className="w-28 h-28 relative">
          <Image src="/images/Bugetify.png" alt="Budgetify-image" fill className="rounded-lg object-cover" />
        </div>
        <p className="ml-4 font-serif text-lg">Budgetify</p>
      </div>

      <div>
        <RegisterForm />
      </div>

      <div className="flex justify-center absolute bottom-0 left-1/2 transform -translate-x-1/2 mb-15">
        <p className="font-serif text-lg">
          Already have an account?
          <Link href="/login">
            <span className="text-blue-500 ml-1">Sign in</span>
          </Link>
        </p>
      </div>
    </div>
  )
}
