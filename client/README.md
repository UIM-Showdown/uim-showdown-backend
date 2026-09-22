# uim-showdown-client

This template should help get you started developing with Vue 3 in Vite.

## Recommended IDE Setup

[VS Code](https://code.visualstudio.com/) + [Vue (Official)](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur).

## Recommended Browser Setup

- Chromium-based browsers (Chrome, Edge, Brave, etc.):
  - [Vue.js devtools](https://chromewebstore.google.com/detail/vuejs-devtools/nhdogjmejiglipccpnnnanhbledajbpd)
  - [Turn on Custom Object Formatter in Chrome DevTools](http://bit.ly/object-formatters)
- Firefox:
  - [Vue.js devtools](https://addons.mozilla.org/en-US/firefox/addon/vue-js-devtools/)
  - [Turn on Custom Object Formatter in Firefox DevTools](https://fxdx.dev/firefox-devtools-custom-object-formatters/)

## Customize configuration

See [Vite Configuration Reference](https://vite.dev/config/).

## Project Setup

```sh
npm install
cp .env.example .env.local
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

#### Local Development Notes
There is a proxy setup via Vite to allow the front end to communicate with the back end on port `8080` by default or whatever endpoint is specified in the `VITE_SERVER_URL` environment variable set in `.env.local` file without any CORS configuration.

This is intended to simulate the final production environment where the front end is served via our Spring Boot back end.

This proxy will also allow developers to use `npm run dev` to hot-reload changes without having to also rebuild the back end.

### Compile and Minify for Production

```sh
npm run build
```

### Lint with [ESLint](https://eslint.org/)

```sh
npm run lint
```
