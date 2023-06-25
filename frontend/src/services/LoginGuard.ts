import router from "@/router"

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
    return {
        sendUserToLoginIfNotLoggedIn
    }
}
