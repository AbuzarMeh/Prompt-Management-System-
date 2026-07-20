import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const promptServiceUrl = env.VITE_PROMPT_SERVICE_URL || 'http://localhost:8000'
  const promptReviewServiceUrl = env.VITE_PROMPT_REVIEW_SERVICE_URL || 'http://localhost:8001'
  const devServerPort = Number(env.VITE_DEV_SERVER_PORT || 5173)

  return {
    plugins: [react()],
    server: {
      port: devServerPort,
      proxy: {
        '/api/prompts': promptServiceUrl,
        '/api/reviews': promptReviewServiceUrl,
      },
    },
  }
})
