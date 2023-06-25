<script setup lang="ts">
import {ref} from "vue"
import {authenticate} from "@/views/Login/components/login-api"
import router from "@/router"

const email = ref("")
const password = ref("")
const errorMessage = ref("") // Add a reactive variable to hold the error message

const onSubmit = async (event: Event) => {
    event.preventDefault()

    try {
        const token = await authenticate(email.value, password.value)
        localStorage.setItem("authToken", JSON.stringify(token))
        router.push({ name: "home" })
    } catch (error) {
        errorMessage.value = "Authentication failed. Please try again."
        // You can also log the error or perform additional error handling actions here
    }
}
</script>

<template>
    <div>
        <h2>Login</h2>
        <form @submit="onSubmit">
            <label for="email">Username:</label>
            <input type="email" id="email" v-model="email" required />
            <br />
            <label for="password">Password:</label>
            <input type="password" id="password" v-model="password" required />
            <br />
            <button type="submit">Login</button>
        </form>

        <!-- Display error message if present -->
        <p v-if="errorMessage">{{ errorMessage }}</p>
    </div>
</template>
