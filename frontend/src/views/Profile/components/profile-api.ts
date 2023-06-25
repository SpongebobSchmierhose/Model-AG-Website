import axios from "axios"

export type Profile = {
    id: number
    firstName: string
    lastName: string
    username: string
    password: string
    roles: Role[]
}

export type Role = {
    id: number
    name: Roles
}

enum Roles {
    ROLE_ADMIN,
    ROLE_MODERATION,
    ROLE_USER
}

export const getProfile = async (accessToken: string | undefined) => {
    const response = await axios.get("http://localhost:8080/api/user", {
        headers: {
            Authorization: "Bearer " + accessToken
        }
    })
    return response.data as Profile
}
