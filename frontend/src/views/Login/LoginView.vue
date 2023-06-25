<script setup lang="ts">
import Login from "@/views/Login/components/Login.vue"
</script>

<template>
    <Suspense>
        <Login></Login>
        <template #fallback>Logging in...</template>
    </Suspense>
</template>
