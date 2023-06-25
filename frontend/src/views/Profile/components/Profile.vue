<script setup lang="ts">
import { useLoginGuard } from "@/services/LoginGuard"
import { ref } from "vue"
import { getProfile } from "@/views/Profile/components/profile-api"

const { getTokenFromLocalStorage } = useLoginGuard()
const username = ref("")
const errorMessage = ref("")
const getUsername = () => {
    try {
        const { accessToken } = getTokenFromLocalStorage()
        getProfile(accessToken).then((profile) => {
            username.value = profile.username
        })
    } catch (error) {
        errorMessage.value = "Authentication failed. Please try again."
    }
}
getUsername()
</script>

<template>
    <div>
        <p>Username: {{ username }}</p>
        <p v-if="errorMessage">{{ errorMessage }}</p>
    </div>
</template>
