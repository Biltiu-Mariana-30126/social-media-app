const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
    transpileDependencies: true,

    devServer: {
        proxy: {
            '/users': {
                target: 'http://localhost:8082',
                changeOrigin: true,
                secure: false
            },
            '/posts': {
                target: 'http://localhost:8082',
                changeOrigin: true,
                secure: false
            }
        }
    },

    pluginOptions: {
        vuetify: {
            // https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vuetify-loader
        }
    }
})
