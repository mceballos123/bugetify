"use client"

import { useState, useEffect } from "react"
import { Line, Pie } from "react-chartjs-2"
import { Chart, registerables } from "chart.js"
import _ from "lodash"
import { getAllTransactions } from "@/app/lib/actions"

Chart.register(...registerables)

// Define interfaces for our transaction data
interface Transaction {
  _id: string
  amount: number
  transactionStatus: string
  paymentDescription: string
  transactionCategory: string
  transactionDate: string
  paymentMethod: string
  recipientInfo: string
}

export default function DashboardForm() {
  const [transaction, setTransaction] = useState<Transaction[]>([])
  const [totalSum, setTotalSum] = useState<number>(0)
  const [transactionsStatus, setTransactionStatus] = useState<Transaction[]>([])
  const [totalSumMonth, setTotalSumMonth] = useState<Record<string, number>>({})
  const [totalCatagorySum, setTotalCatagorySum] = useState<Record<string, number>>({})
  const [selectGraph, setSelectGraph] = useState("default")
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

  const transactionByMonths = () => {
    const monthlyAmount = _.mapValues(
      _.groupBy(transaction, ({ transactionDate }: Transaction) => new Date(transactionDate).getMonth()),
      (group: Transaction[]) => _.sumBy(group, (item: Transaction) => Number(item.amount)),
    )

    setTotalSumMonth(monthlyAmount)
  }

  useEffect(() => {
    transactionByMonths()
  }, [transaction])

  useEffect(() => {
    const search = transaction.filter((item: Transaction) => item.transactionStatus === "completed")
    setTransactionStatus(search)

    const total = transaction.reduce((acc: number, currentValue: Transaction) => acc + Number(currentValue.amount), 0)
    setTotalSum(total)
  }, [transaction])

  const catagorySum = () => {
    const groupedByCategory = _.groupBy(transaction, "transactionCategory")

    const categoryAmount = _.mapValues(groupedByCategory, (group: Transaction[]) => {
      return _.sumBy(group, (item: Transaction) => Number(item.amount))
    })

    setTotalCatagorySum(categoryAmount)
  }

  useEffect(() => {
    catagorySum()
  }, [transaction])

  const dataAmount = {
    labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
    datasets: [
      {
        label: "Monthly Spending",
        data: [
          totalSumMonth["0"] || 0,
          totalSumMonth["1"] || 0,
          totalSumMonth["2"] || 0,
          totalSumMonth["3"] || 0,
          totalSumMonth["4"] || 0,
          totalSumMonth["5"] || 0,
          totalSumMonth["6"] || 0,
          totalSumMonth["7"] || 0,
          totalSumMonth["8"] || 0,
          totalSumMonth["9"] || 0,
          totalSumMonth["10"] || 0,
          totalSumMonth["11"] || 0,
        ],
        borderColor: "rgb(75, 192, 192)",
        backgroundColor: "rgb(255, 255, 255)",
      },
    ],
  }

  const config = {
    type: "line",
    data: dataAmount,
    options: {
      animations: {
        tension: {
          duration: 1000,
          easing: "linear",
          from: 1,
          to: 0,
          loop: true,
        },
      },
      scales: {
        y: {
          ticks: {
            callback: (value: any) => "$" + value,
          },
          min: 0,
          max: 100,
        },
      },
      plugins: {
        tooltip: {
          callbacks: {
            label: (tooltipItem: any) => "$" + tooltipItem.raw,
          },
        },
      },
    },
  }

  const roundToTwoDecimal = (numExample: number) => {
    return Math.round(numExample * 100) / 100
  }

  const orderedCategories = ["Personal Expenses", "Food", "Entertainment", "Utilities", "Other"]
  const dataValues = orderedCategories.map((catagory: string) => totalCatagorySum[catagory] || 0)

  const pieData = {
    labels: orderedCategories,
    datasets: [
      {
        label: "Catagory Sum",
        data: dataValues,
        backgroundColor: [
          "rgb(220, 20, 60)", // Red (Crimson)
          "rgb(30, 144, 255)", // Blue (DodgerBlue)
          "rgb(34, 139, 34)", // Green (ForestGreen)
          "rgb(128, 0, 128)", // Purple
          "rgb(255, 140, 0)", // Orange (DarkOrange)
        ],
        hoverOffset: 4,
      },
    ],
  }

  const configPieGraph = {
    type: "pie",
    data: pieData,
    options: {
      responsive: true,
      plugins: {
        legend: {
          position: "top",
        },
        title: {
          display: true,
          text: "Category Sum",
        },
      },
    },
  }

  const LineChartComponent = () => (
    <div className="flex flex-row mb-8 mt-1">
      <Line options={config as any} data={dataAmount} />
    </div>
  )

  const PieChartComponent = () => (
    <div className="flex flex-row mt-[140px] ml-[330px]">
      <div className="h-[260px] w-[330px]">
        <Pie options={configPieGraph as any} data={pieData} />
      </div>
    </div>
  )

  const componentMap = {
    default: <LineChartComponent />,
    pieChart: <PieChartComponent />,
  }

  if (isLoading) {
    return (
      <div className="h-[710px] w-[960px] rounded-2xl border-solid bg-white ml-[390px] mt-[59px] relative flex items-center justify-center">
        <p className="text-xl font-serif">Loading dashboard data...</p>
      </div>
    )
  }

  return (
    <div className="h-[710px] w-[960px] rounded-2xl border-solid bg-white ml-[390px] mt-[59px] relative">
      <div className="ml-4">
        <h1 className="mt-8 font-serif text-3xl font-bold">Finance Dashboard</h1>

        {/* Stats Section */}
        <div className="mt-6 flex gap-6">
          {/* Total Expenses Box */}
          <div className="w-[420px] h-[130px] bg-white p-4 rounded-2xl border border-gray-350">
            <h2 className="text-xl font-serif font-bold ml-4">Total Expenses</h2>
            <p className="font-serif text-2xl font-bold mt-2 ml-4">&#x24; {roundToTwoDecimal(totalSum)}</p>
          </div>

          <div className="ml-[92px] flex flex-col">
            <div className="w-[380px] h-[130px] bg-white p-4 rounded-2xl border border-gray-350">
              <h1 className="font-serif text-xl font-bold ml-4">Chart Type</h1>
              <label htmlFor="chartTypeSelect" className="sr-only">Select Chart Type</label>
              <select
                id="chartTypeSelect"
                className="ml-4 border h-[45px] w-[300px] border-gray-350 rounded-sm mt-4 font-serif text-sm focus:ring-gray-300 focus:border-gray-500 p-2"
                onChange={(e) => setSelectGraph(e.target.value)}
                value={selectGraph}
              >
                <option value="default" className="font-serif">
                  Line Chart
                </option>
                <option value="pieChart" className="font-serif">
                  Pie Chart
                </option>
              </select>
            </div>
          </div>
        </div>

        {componentMap[selectGraph as keyof typeof componentMap]}
      </div>
    </div>
  )
}
