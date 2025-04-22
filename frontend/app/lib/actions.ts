"use server"

// Register user
export async function registerUser(data: any) {
  try {
    const response = await fetch("http://localhost:3000/user/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    })

    const result = await response.json()

    if (!response.ok) {
      throw new Error(result.message || "Failed to register")
    }

    return result
  } catch (error: any) {
    console.error("Registration error:", error)
    throw new Error(error.message || "Failed to register")
  }
}

// Login user
export async function loginUser(data: any) {
  try {
    const response = await fetch("http://localhost:3000/user/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    })

    const result = await response.json()

    if (!response.ok) {
      throw new Error(result.message || "Failed to login")
    }

    return result
  } catch (error: any) {
    console.error("Login error:", error)
    throw new Error(error.message || "Failed to login")
  }
}

// Get API Key
export async function getAPIKey() {
  const response = await fetch("http://localhost:3000/apiKey")
  if (!response.ok) {
    throw new Error("Failed to get API key")
  }
  return response.json()
}

// Get JWT Secret Key
export async function getJWTSecretKey() {
  const response = await fetch("http://localhost:3000/jwtSecretKey")
  if (!response.ok) {
    throw new Error("Failed to get JWT secret key")
  }
  return response.json()
}

// Create transaction
export async function createTransaction(data: any) {
  try {
    const response = await fetch("http://localhost:3000/createTransaction", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    })

    const result = await response.json()

    if (!response.ok) {
      throw new Error(result.message || "Failed to create transaction")
    }

    return result
  } catch (error: any) {
    console.error("Create transaction error:", error)
    throw new Error(error.message || "Failed to create transaction")
  }
}

// Get all transactions
export async function getAllTransactions() {
  try {
    const apiKey = await getAPIKey()
    const response = await fetch("http://localhost:3000/transaction", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "X-RapidAPI-Key": apiKey,
      },
    })

    const result = await response.json()

    if (!response.ok) {
      throw new Error(result.message || "Failed to get transactions")
    }

    return result
  } catch (error: any) {
    console.error("Get transactions error:", error)
    throw new Error(error.message || "Failed to get transactions")
  }
}

// Get specific transaction
export async function getTransaction(id: string) {
  try {
    const apiKey = await getAPIKey()
    const response = await fetch(`http://localhost:3000/transaction/${id}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "X-RapidAPI-Key": apiKey,
      },
    })

    const result = await response.json()

    if (!response.ok) {
      throw new Error(result.message || "Failed to get transaction")
    }

    return result
  } catch (error: any) {
    console.error("Get transaction error:", error)
    throw new Error(error.message || "Failed to get transaction")
  }
}

// Update transaction
export async function updateTransaction(id: string, data: any) {
  try {
    const response = await fetch(`http://localhost:3000/transaction/update/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    })

    const result = await response.json()

    if (!response.ok) {
      throw new Error(result.message || "Failed to update transaction")
    }

    return result
  } catch (error: any) {
    console.error("Update transaction error:", error)
    throw new Error(error.message || "Failed to update transaction")
  }
}

// Delete transaction
export async function deleteTransactionById(id: string) {
  try {
    const jwtToken = await getJWTSecretKey()
    const response = await fetch(`http://localhost:3000/transaction/delete/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${jwtToken}`,
      },
      body: JSON.stringify({ id }),
    })

    if (!response.ok) {
      throw new Error("Failed to delete transaction")
    }

    return true
  } catch (error: any) {
    console.error("Delete transaction error:", error)
    throw new Error(error.message || "Failed to delete transaction")
  }
}
