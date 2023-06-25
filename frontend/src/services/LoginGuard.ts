import router from "@/router"
import type { AuthToken } from "@/views/Login/components/login-api"

export const useLoginGuard = () => {
    const sendUserToLoginIfNotLoggedIn = () => {
        router.beforeEach((to) => {
            if (!isAuthenticated() && to.name !== "login") {
                return { name: "login" }
            }
        })
    }

    const isAuthenticated = () => {
        const savedToken = localStorage.getItem("authToken")
        return savedToken !== null
    }

    const getTokenFromLocalStorage = (): AuthToken => {
        const savedToken = localStorage.getItem("authToken")
        const authToken: AuthToken | null = savedToken ? JSON.parse(savedToken) : null
        if (authToken) {
            return authToken
        } else {
            throw new Error("Something went wrong")
        }
    }
    return {
        sendUserToLoginIfNotLoggedIn,
        getTokenFromLocalStorage
    }
}
