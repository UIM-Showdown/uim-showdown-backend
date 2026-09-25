import { fileURLToPath, URL } from 'node:url'

import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd());

  return {
    plugins: [
      vue(),
      vueDevTools(),
    ],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url)),
      },
    },
    server: {
      proxy: {
        '/api': {
          target: env.VITE_SERVER_URL ?? 'http://localhost:8080',
          changeOrigin: true,
          secure: false,
          // backend is not expecting '/api' prefix
          rewrite: (path) => path.replace(/^\/api/, ''),
        },
      }
    },
    css: {
      preprocessorOptions: {
        scss: {
          loadPaths: ['node_modules/@picocss/pico/scss'],
          quietDeps: true,
        }
      }
    }
  };
});
