import type { Config } from 'tailwindcss';
import daisyui from 'daisyui';
import typography from '@tailwindcss/typography';

const config: Config = {
  content: ['./src/**/*.{html,ts}'],
  darkMode: 'selector',
  theme: {
    extend: {
      animation: {
        'infinite-scroll': 'infinite-scroll 25s linear infinite',
      },
      keyframes: {
        'infinite-scroll': {
          from: { transform: 'translateX(0)' },
          to: { transform: 'translateX(-100%)' },
        },
      },
    },
  },
  plugins: [typography, daisyui],
  daisyui: {
    themes: ['light', 'dark'],
  },
};

export default config;
