import colors from 'tailwindcss/colors'

export default {
  content: [
    './index.html',
    './src/**/*.{vue,js,ts,jsx,tsx}'
  ],
  theme: {
    extend: {
      colors: {
        ...colors, // ajoute toutes les couleurs Tailwind (dont rose-600)
        primary: {
          DEFAULT: '#e11d48',
          dark: '#be123c',
        },
      },
    },
  },
  plugins: [],
}
