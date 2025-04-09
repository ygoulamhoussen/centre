<template>
    <div class="rounded-2xl shadow-lg overflow-hidden bg-white hover:shadow-xl transition">
      <img :src="image" alt="" class="w-full h-48 object-cover">
      <div class="p-4">
        <h3 class="text-lg font-semibold">{{ title }}</h3>
        <p class="text-sm text-gray-500">{{ location }}</p>
        <p class="mt-2 text-primary font-bold">{{ price }} €/mois</p>
      </div>
    </div>
  </template>
  
  <script setup>
  defineProps({
    title: String,
    location: String,
    price: Number,
    image: String
  })
  </script>
  