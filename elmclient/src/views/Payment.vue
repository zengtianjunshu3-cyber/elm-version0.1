<template>
	<div class="wrapper">

		<!-- header部分 -->
		<header>
			<p>在线支付</p>
		</header>

		<!-- 订单信息部分 -->
		<h3>订单信息：</h3>
		<div class="order-info">
			<p>
				{{orders.business.businessName}}
				<i class="fa fa-caret-down" @click="detailetShow"></i>
			</p>
			<p>&#165;{{orders.orderTotal}}</p>
		</div>

		<!-- 订单明细部分 -->
		<ul class="order-detailet" v-show="isShowDetailet">
			<li v-for="item in orders.list">
				<p>{{item.food.foodName}} x {{item.quantity}}</p>
				<p>&#165;{{item.food.foodPrice*item.quantity}}</p>
			</li>
			<li>
				<p>配送费</p>
				<p>&#165;{{orders.business.deliveryPrice}}</p>
			</li>
		</ul>

		<!-- 红包抵扣部分 -->
		<div class="discount-section" v-if="user.redEnvelope > 0">
			<div class="discount-left">
				<i class="fa fa-gift"></i>
				<span>红包抵扣 (余额: &#165;{{user.redEnvelope}})</span>
			</div>
			<div class="discount-right">
				<input type="checkbox" v-model="useRedEnvelope">
				<span class="discount-amount" v-if="useRedEnvelope">-&#165;{{appliedDiscount}}</span>
			</div>
		</div>

		<!-- 支付方式部分 -->
		<ul class="payment-type">
			<li>
				<img src="../assets/alipay.png">
				<i class="fa fa-check-circle"></i>
			</li>
			<li>
				<img src="../assets/wechat.png">
			</li>
		</ul>
		<div class="payment-button">
			<button @click="pay" style="z-index: 10001; position: relative;">确认支付 &#165;{{finalPayAmount}}</button>
		</div>

		<div style="height: 20vw;"></div>

		<!-- 底部菜单部分 -->
		<Footer></Footer>
	</div>
</template>

<script>
	import Footer from '../components/Footer.vue';

	export default {
		name: 'Payment',
		data() {
			return {
				orderId: this.$route.query.orderId,
				orders: {
					business: {}
				},
				user: {},
				isShowDetailet: false,
				useRedEnvelope: false
			}
		},
		computed: {
			appliedDiscount() {
				if (!this.useRedEnvelope || !this.user.redEnvelope) return 0;
				// 红包不能超过订单总额
				return Math.min(this.user.redEnvelope, this.orders.orderTotal).toFixed(2);
			},
			finalPayAmount() {
				if (!this.orders.orderTotal) return 0;
				return (this.orders.orderTotal - this.appliedDiscount).toFixed(2);
			}
		},
		created() {
			this.user = this.$getSessionStorage('user');
			// 获取最新的用户信息（确保红包余额准确）
			this.$axios.post('UserController/getUserById', this.$qs.stringify({
				userId: this.user.userId
			})).then(response => {
				if (response.data) {
					this.user = response.data;
					this.$setSessionStorage('user', this.user);
				}
			});

			this.$axios.post('OrdersController/getOrdersById', this.$qs.stringify({
				orderId: this.orderId
			})).then(response => {
				this.orders = response.data;
			}).catch(error => {
				console.error(error);
			});
		},
		mounted() {
			//这里的代码是实现：一旦路由到在线支付组件，就不能回到订单确认组件。 
			//先将当前url添加到history对象中 
			history.pushState(null, null, document.URL);
			//popstate事件能够监听history对象的变化 
			window.onpopstate = () => {
				this.$router.push({
					path: '/index'
				});
			}
		},
		destroyed() {
			window.onpopstate = null;
		},
		methods: {
			detailetShow() {
				this.isShowDetailet = !this.isShowDetailet;
			},
			pay() {
				this.$axios.post('OrdersController/updateOrdersState', this.$qs.stringify({
					orderId: this.orderId,
					orderState: 1,
					usedRedEnvelope: this.appliedDiscount,
					orderTotal: this.finalPayAmount // 更新为实际支付金额
				})).then(response => {
					const data = response.data;
					if (data.result > 0) {
						// 支付成功后，立即更新 Session 中的用户信息
						this.$axios.post('UserController/getUserById', this.$qs.stringify({
							userId: this.user.userId
						})).then(res => {
							if (res.data) {
								this.$setSessionStorage('user', res.data);
							}
							alert(`支付成功！\n本次获得奖励：\n红包：￥${data.rewardRed}\n金币：${data.rewardGold}个`);
							this.$router.push({
								path: '/index'
							});
						});
					} else {
						alert('支付失败！');
					}
				}).catch(error => {
					console.error(error);
				});
			}
		},
		components: {
			Footer
		}
	}
</script>

<style scoped>
	/****************** 总容器 ******************/
	.wrapper {
		width: 100%;
		height: 100%;
	}

	/****************** header部分 ******************/
	.wrapper header {
		width: 100%;
		height: 12vw;
		background-color: #0097FF;
		color: #fff;
		font-size: 4.8vw;

		position: fixed;
		left: 0;
		top: 0;
		z-index: 1000;

		display: flex;
		justify-content: center;
		align-items: center;
	}

	/****************** 订单信息部分 ******************/
	.wrapper h3 {
		margin-top: 12vw;
		box-sizing: border-box;
		padding: 4vw 4vw 0;

		font-size: 4vw;
		font-weight: 300;
		color: #999;
	}

	.wrapper .order-info {
		box-sizing: border-box;
		padding: 4vw;
		font-size: 4vw;
		color: #666;

		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.wrapper .order-info p:last-child {
		color: orangered;
	}

	/****************** 订单明细部分 ******************/
	.wrapper .order-detailet {
		width: 100%;
	}

	.wrapper .order-detailet li {
		width: 100%;
		box-sizing: border-box;
		padding: 1vw 4vw;

		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.wrapper .order-detailet li p {
		font-size: 3vw;
		color: #666;
	}

	/****************** 红包抵扣部分 ******************/
	.discount-section {
		width: 100%;
		box-sizing: border-box;
		padding: 4vw;
		background-color: #FFFDF0;
		border-top: 1px solid #f0f0f0;
		border-bottom: 1px solid #f0f0f0;
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin: 2vw 0;
	}

	.discount-left {
		display: flex;
		align-items: center;
		color: #E67E22;
		font-size: 3.8vw;
	}

	.discount-left i {
		font-size: 5vw;
		margin-right: 2vw;
	}

	.discount-right {
		display: flex;
		align-items: center;
	}

	.discount-right input {
		width: 5vw;
		height: 5vw;
		margin-right: 2vw;
	}

	.discount-amount {
		color: #ff4d4f;
		font-weight: 700;
		font-size: 4vw;
	}

	/****************** 支付方式部分 ******************/
	.wrapper .payment-type {
		width: 100%;
	}

	.wrapper .payment-type li {
		width: 100%;
		box-sizing: border-box;
		padding: 4vw;

		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.wrapper .payment-type li img {
		width: 33vw;
		height: 8.9vw;
	}

	.wrapper .payment-type li .fa-check-circle {
		font-size: 5vw;
		color: #38CA73;
	}

	.wrapper .payment-button {
		width: 100%;
		box-sizing: border-box;
		padding: 4vw;
		position: fixed;
		bottom: 14vw; /* 确保在 Footer 之上 */
		left: 0;
	}

	.wrapper .payment-button button {
		width: 100%;
		height: 12vw;
		border: none;
		border-radius: 2vw;
		background-color: #4cd964;
		color: #fff;
		font-size: 4.5vw;
		font-weight: 700;
		cursor: pointer;
	}
</style>
