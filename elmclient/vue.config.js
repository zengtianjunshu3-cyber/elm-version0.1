module.exports = {
    devServer: {
        port: 8087,
        host: '0.0.0.0',
        client: {
            overlay: false,
        },
        proxy: {
            // 只代理以 Controller 结尾的后端接口
            '.*Controller': {
                target: 'http://localhost:8080/elm',
                changeOrigin: true
            }
        }
    }
}
