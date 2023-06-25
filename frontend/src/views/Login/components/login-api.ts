import axios from "axios"

export interface AuthToken {
    accessToken: string
    refreshToken: string
}

export const authenticate = async (email: string, password: string) => {
    const credentials = { username: email, password: password }
    try {
        const response = await axios.post(
            "http://localhost:8080/api/login",
            getQueryString(credentials),
            {
                headers: {
                    "content-type": "application/x-www-form-urlencoded"
                }
            }
        )
        return {
            accessToken: response.data.access_token,
            refreshToken: response.data.refresh_token
        } as AuthToken
    } catch (error) {
        throw new Error("Authentication failed. Please try again.")
    }
}

export const getQueryString = (data = {}) => {
    return Object.entries(data)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value as string)}`)
        .join("&")
}
