<script setup lang="ts">
import Profile from "@/views/Profile/components/Profile.vue"
</script>

<template>
    <Suspense>
        <Profile></Profile>
        <template #fallback>Loading...</template>
    </Suspense>
</template>
