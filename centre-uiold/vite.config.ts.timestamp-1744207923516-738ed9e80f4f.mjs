// vite.config.ts
import process from "node:process";
import { URL, fileURLToPath } from "node:url";
import { defineConfig } from "file:///C:/git/centre/centre-ui/node_modules/vite/dist/node/index.js";
import vue from "file:///C:/git/centre/centre-ui/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import vueJsx from "file:///C:/git/centre/centre-ui/node_modules/@vitejs/plugin-vue-jsx/dist/index.mjs";
import VueDevTools from "file:///C:/git/centre/centre-ui/node_modules/vite-plugin-vue-devtools/dist/vite.mjs";
import { VitePWA } from "file:///C:/git/centre/centre-ui/node_modules/vite-plugin-pwa/dist/index.js";
import AutoImport from "file:///C:/git/centre/centre-ui/node_modules/unplugin-auto-import/dist/vite.js";
import Components from "file:///C:/git/centre/centre-ui/node_modules/unplugin-vue-components/dist/vite.js";
import {
  vueDsfrAutoimportPreset,
  vueDsfrComponentResolver
} from "file:///C:/git/centre/centre-ui/node_modules/@gouvminint/vue-dsfr/dist/vue-dsfr.js";
var __vite_injected_original_import_meta_url = "file:///C:/git/centre/centre-ui/vite.config.ts";
var vite_config_default = defineConfig({
  plugins: [
    vue(),
    vueJsx(),
    VueDevTools(),
    VitePWA({
      registerType: "autoUpdate",
      includeAssets: ["favicon.svg", "safari-pinned-tab.svg"],
      workbox: {
        maximumFileSizeToCacheInBytes: 3e6
        // Pour le CSS du DSFR :-/
      },
      manifest: {
        name: "Dummy app",
        short_name: "Dummy",
        theme_color: "#ffffff",
        background_color: "#ffffff",
        icons: [
          {
            src: "/android-chrome-192x192.png",
            sizes: "192x192",
            type: "image/png"
          },
          {
            src: "/android-chrome-512x512.png",
            sizes: "512x512",
            type: "image/png"
          }
        ],
        display: "standalone"
      }
    }),
    AutoImport({
      include: [
        /\.[tj]sx?$/,
        /\.vue$/,
        /\.vue\?vue/
      ],
      imports: [
        // @ts-expect-error TS2322
        "vue",
        // @ts-expect-error TS2322
        "vue-router",
        // @ts-expect-error TS2322
        "pinia",
        // @ts-expect-error TS2322
        "vitest",
        // @ts-expect-error TS2322
        vueDsfrAutoimportPreset
      ],
      vueTemplate: true,
      dts: "./src/auto-imports.d.ts",
      eslintrc: {
        enabled: true,
        filepath: "./.eslintrc-auto-import.json",
        globalsPropValue: true
      }
    }),
    Components({
      extensions: ["vue"],
      dirs: ["src/components"],
      // Autoimport de vos composants qui sont dans le dossier `src/components`
      include: [/\.vue$/, /\.vue\?vue/],
      dts: "./src/components.d.ts",
      resolvers: [
        vueDsfrComponentResolver
        // Autoimport des composants de VueDsfr dans les templates
      ]
    })
  ],
  base: process.env.BASE_URL || "/",
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", __vite_injected_original_import_meta_url))
    },
    dedupe: ["vue"]
  }
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcudHMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJDOlxcXFxnaXRcXFxcY2VudHJlXFxcXGNlbnRyZS11aVwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiQzpcXFxcZ2l0XFxcXGNlbnRyZVxcXFxjZW50cmUtdWlcXFxcdml0ZS5jb25maWcudHNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0M6L2dpdC9jZW50cmUvY2VudHJlLXVpL3ZpdGUuY29uZmlnLnRzXCI7aW1wb3J0IHByb2Nlc3MgZnJvbSAnbm9kZTpwcm9jZXNzJ1xuaW1wb3J0IHsgVVJMLCBmaWxlVVJMVG9QYXRoIH0gZnJvbSAnbm9kZTp1cmwnXG5cbmltcG9ydCB7IGRlZmluZUNvbmZpZyB9IGZyb20gJ3ZpdGUnXG5pbXBvcnQgdnVlIGZyb20gJ0B2aXRlanMvcGx1Z2luLXZ1ZSdcbmltcG9ydCB2dWVKc3ggZnJvbSAnQHZpdGVqcy9wbHVnaW4tdnVlLWpzeCdcbmltcG9ydCBWdWVEZXZUb29scyBmcm9tICd2aXRlLXBsdWdpbi12dWUtZGV2dG9vbHMnXG5pbXBvcnQgeyBWaXRlUFdBIH0gZnJvbSAndml0ZS1wbHVnaW4tcHdhJ1xuaW1wb3J0IEF1dG9JbXBvcnQgZnJvbSAndW5wbHVnaW4tYXV0by1pbXBvcnQvdml0ZSdcbmltcG9ydCBDb21wb25lbnRzIGZyb20gJ3VucGx1Z2luLXZ1ZS1jb21wb25lbnRzL3ZpdGUnXG5pbXBvcnQge1xuICB2dWVEc2ZyQXV0b2ltcG9ydFByZXNldCxcbiAgdnVlRHNmckNvbXBvbmVudFJlc29sdmVyLFxufSBmcm9tICdAZ291dm1pbmludC92dWUtZHNmcidcblxuLy8gaHR0cHM6Ly92aXRlanMuZGV2L2NvbmZpZy9cbmV4cG9ydCBkZWZhdWx0IGRlZmluZUNvbmZpZyh7XG4gIHBsdWdpbnM6IFtcbiAgICB2dWUoKSxcbiAgICB2dWVKc3goKSxcbiAgICBWdWVEZXZUb29scygpLFxuICAgIFZpdGVQV0Eoe1xuICAgICAgcmVnaXN0ZXJUeXBlOiAnYXV0b1VwZGF0ZScsXG4gICAgICBpbmNsdWRlQXNzZXRzOiBbJ2Zhdmljb24uc3ZnJywgJ3NhZmFyaS1waW5uZWQtdGFiLnN2ZyddLFxuICAgICAgd29ya2JveDoge1xuICAgICAgICBtYXhpbXVtRmlsZVNpemVUb0NhY2hlSW5CeXRlczogMzAwMDAwMCAvLyBQb3VyIGxlIENTU1x1MDBBMGR1IERTRlIgOi0vXG4gICAgICB9LFxuICAgICAgbWFuaWZlc3Q6IHtcbiAgICAgICAgbmFtZTogJ0R1bW15IGFwcCcsXG4gICAgICAgIHNob3J0X25hbWU6ICdEdW1teScsXG4gICAgICAgIHRoZW1lX2NvbG9yOiAnI2ZmZmZmZicsXG4gICAgICAgIGJhY2tncm91bmRfY29sb3I6ICcjZmZmZmZmJyxcbiAgICAgICAgaWNvbnM6IFtcbiAgICAgICAgICB7XG4gICAgICAgICAgICBzcmM6ICcvYW5kcm9pZC1jaHJvbWUtMTkyeDE5Mi5wbmcnLFxuICAgICAgICAgICAgc2l6ZXM6ICcxOTJ4MTkyJyxcbiAgICAgICAgICAgIHR5cGU6ICdpbWFnZS9wbmcnLFxuICAgICAgICAgIH0sXG4gICAgICAgICAge1xuICAgICAgICAgICAgc3JjOiAnL2FuZHJvaWQtY2hyb21lLTUxMng1MTIucG5nJyxcbiAgICAgICAgICAgIHNpemVzOiAnNTEyeDUxMicsXG4gICAgICAgICAgICB0eXBlOiAnaW1hZ2UvcG5nJyxcbiAgICAgICAgICB9LFxuICAgICAgICBdLFxuICAgICAgICBkaXNwbGF5OiAnc3RhbmRhbG9uZScsXG4gICAgICB9LFxuICAgIH0pLFxuICAgIEF1dG9JbXBvcnQoe1xuICAgICAgaW5jbHVkZTogW1xuICAgICAgICAvXFwuW3RqXXN4PyQvLFxuICAgICAgICAvXFwudnVlJC8sXG4gICAgICAgIC9cXC52dWVcXD92dWUvLFxuICAgICAgXSxcbiAgICAgIGltcG9ydHM6IFtcbiAgICAgICAgLy8gQHRzLWV4cGVjdC1lcnJvciBUUzIzMjJcbiAgICAgICAgJ3Z1ZScsXG4gICAgICAgIC8vIEB0cy1leHBlY3QtZXJyb3IgVFMyMzIyXG4gICAgICAgICd2dWUtcm91dGVyJyxcbiAgICAgICAgLy8gQHRzLWV4cGVjdC1lcnJvciBUUzIzMjJcbiAgICAgICAgJ3BpbmlhJyxcbiAgICAgICAgLy8gQHRzLWV4cGVjdC1lcnJvciBUUzIzMjJcbiAgICAgICAgJ3ZpdGVzdCcsXG4gICAgICAgIC8vIEB0cy1leHBlY3QtZXJyb3IgVFMyMzIyXG4gICAgICAgIHZ1ZURzZnJBdXRvaW1wb3J0UHJlc2V0LFxuICAgICAgXSxcbiAgICAgIHZ1ZVRlbXBsYXRlOiB0cnVlLFxuICAgICAgZHRzOiAnLi9zcmMvYXV0by1pbXBvcnRzLmQudHMnLFxuICAgICAgZXNsaW50cmM6IHtcbiAgICAgICAgZW5hYmxlZDogdHJ1ZSxcbiAgICAgICAgZmlsZXBhdGg6ICcuLy5lc2xpbnRyYy1hdXRvLWltcG9ydC5qc29uJyxcbiAgICAgICAgZ2xvYmFsc1Byb3BWYWx1ZTogdHJ1ZSxcbiAgICAgIH0sXG4gICAgfSksXG4gICAgQ29tcG9uZW50cyh7XG4gICAgICBleHRlbnNpb25zOiBbJ3Z1ZSddLFxuICAgICAgZGlyczogWydzcmMvY29tcG9uZW50cyddLCAvLyBBdXRvaW1wb3J0IGRlIHZvcyBjb21wb3NhbnRzIHF1aSBzb250IGRhbnMgbGUgZG9zc2llciBgc3JjL2NvbXBvbmVudHNgXG4gICAgICBpbmNsdWRlOiBbL1xcLnZ1ZSQvLCAvXFwudnVlXFw/dnVlL10sXG4gICAgICBkdHM6ICcuL3NyYy9jb21wb25lbnRzLmQudHMnLFxuICAgICAgcmVzb2x2ZXJzOiBbXG4gICAgICAgIHZ1ZURzZnJDb21wb25lbnRSZXNvbHZlciwgLy8gQXV0b2ltcG9ydCBkZXMgY29tcG9zYW50cyBkZSBWdWVEc2ZyIGRhbnMgbGVzIHRlbXBsYXRlc1xuICAgICAgXSxcbiAgICB9KSxcbiAgXSxcbiAgYmFzZTogcHJvY2Vzcy5lbnYuQkFTRV9VUkwgfHwgJy8nLFxuICByZXNvbHZlOiB7XG4gICAgYWxpYXM6IHtcbiAgICAgICdAJzogZmlsZVVSTFRvUGF0aChuZXcgVVJMKCcuL3NyYycsIGltcG9ydC5tZXRhLnVybCkpLFxuICAgIH0sXG4gICAgZGVkdXBlOiBbJ3Z1ZSddLFxuICB9LFxufSlcbiJdLAogICJtYXBwaW5ncyI6ICI7QUFBK1AsT0FBTyxhQUFhO0FBQ25SLFNBQVMsS0FBSyxxQkFBcUI7QUFFbkMsU0FBUyxvQkFBb0I7QUFDN0IsT0FBTyxTQUFTO0FBQ2hCLE9BQU8sWUFBWTtBQUNuQixPQUFPLGlCQUFpQjtBQUN4QixTQUFTLGVBQWU7QUFDeEIsT0FBTyxnQkFBZ0I7QUFDdkIsT0FBTyxnQkFBZ0I7QUFDdkI7QUFBQSxFQUNFO0FBQUEsRUFDQTtBQUFBLE9BQ0s7QUFic0osSUFBTSwyQ0FBMkM7QUFnQjlNLElBQU8sc0JBQVEsYUFBYTtBQUFBLEVBQzFCLFNBQVM7QUFBQSxJQUNQLElBQUk7QUFBQSxJQUNKLE9BQU87QUFBQSxJQUNQLFlBQVk7QUFBQSxJQUNaLFFBQVE7QUFBQSxNQUNOLGNBQWM7QUFBQSxNQUNkLGVBQWUsQ0FBQyxlQUFlLHVCQUF1QjtBQUFBLE1BQ3RELFNBQVM7QUFBQSxRQUNQLCtCQUErQjtBQUFBO0FBQUEsTUFDakM7QUFBQSxNQUNBLFVBQVU7QUFBQSxRQUNSLE1BQU07QUFBQSxRQUNOLFlBQVk7QUFBQSxRQUNaLGFBQWE7QUFBQSxRQUNiLGtCQUFrQjtBQUFBLFFBQ2xCLE9BQU87QUFBQSxVQUNMO0FBQUEsWUFDRSxLQUFLO0FBQUEsWUFDTCxPQUFPO0FBQUEsWUFDUCxNQUFNO0FBQUEsVUFDUjtBQUFBLFVBQ0E7QUFBQSxZQUNFLEtBQUs7QUFBQSxZQUNMLE9BQU87QUFBQSxZQUNQLE1BQU07QUFBQSxVQUNSO0FBQUEsUUFDRjtBQUFBLFFBQ0EsU0FBUztBQUFBLE1BQ1g7QUFBQSxJQUNGLENBQUM7QUFBQSxJQUNELFdBQVc7QUFBQSxNQUNULFNBQVM7QUFBQSxRQUNQO0FBQUEsUUFDQTtBQUFBLFFBQ0E7QUFBQSxNQUNGO0FBQUEsTUFDQSxTQUFTO0FBQUE7QUFBQSxRQUVQO0FBQUE7QUFBQSxRQUVBO0FBQUE7QUFBQSxRQUVBO0FBQUE7QUFBQSxRQUVBO0FBQUE7QUFBQSxRQUVBO0FBQUEsTUFDRjtBQUFBLE1BQ0EsYUFBYTtBQUFBLE1BQ2IsS0FBSztBQUFBLE1BQ0wsVUFBVTtBQUFBLFFBQ1IsU0FBUztBQUFBLFFBQ1QsVUFBVTtBQUFBLFFBQ1Ysa0JBQWtCO0FBQUEsTUFDcEI7QUFBQSxJQUNGLENBQUM7QUFBQSxJQUNELFdBQVc7QUFBQSxNQUNULFlBQVksQ0FBQyxLQUFLO0FBQUEsTUFDbEIsTUFBTSxDQUFDLGdCQUFnQjtBQUFBO0FBQUEsTUFDdkIsU0FBUyxDQUFDLFVBQVUsWUFBWTtBQUFBLE1BQ2hDLEtBQUs7QUFBQSxNQUNMLFdBQVc7QUFBQSxRQUNUO0FBQUE7QUFBQSxNQUNGO0FBQUEsSUFDRixDQUFDO0FBQUEsRUFDSDtBQUFBLEVBQ0EsTUFBTSxRQUFRLElBQUksWUFBWTtBQUFBLEVBQzlCLFNBQVM7QUFBQSxJQUNQLE9BQU87QUFBQSxNQUNMLEtBQUssY0FBYyxJQUFJLElBQUksU0FBUyx3Q0FBZSxDQUFDO0FBQUEsSUFDdEQ7QUFBQSxJQUNBLFFBQVEsQ0FBQyxLQUFLO0FBQUEsRUFDaEI7QUFDRixDQUFDOyIsCiAgIm5hbWVzIjogW10KfQo=
