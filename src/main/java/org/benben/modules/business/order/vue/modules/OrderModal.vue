<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <a-form :form="form">
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="用户id">
              <a-input placeholder="请输入用户id" v-decorator="['userId', {}]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="骑手id">
              <a-input placeholder="请输入骑手id" v-decorator="['riderId', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="商品数量">
              <a-input-number placeholder="请输入商品数量" style="width:100%" v-decorator="[ 'goodsCount', {}]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="门店id">
              <a-input placeholder="请输入门店id" v-decorator="['storeId', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="订单编号">
              <a-input placeholder="请输入订单编号" v-decorator="['orderId', {}]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="商品总金额">
              <a-input-number placeholder="请输入商品总金额" style="width:100%" v-decorator="[ 'goodsMoney', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="订单总金额">
              <a-input-number placeholder="请输入订单总金额" style="width:100%" v-decorator="[ 'orderMoney', {}]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="订单类型(0:送餐  1：店内用餐)">
              <a-input placeholder="请输入订单类型(0:送餐  1：店内用餐)" v-decorator="['orderType', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="送餐地址">
              <a-input placeholder="请输入送餐地址" v-decorator="['userAddress', {}]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="用户电话">
              <a-input placeholder="请输入用户电话" v-decorator="['usedPhone', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="骑手电话">
              <a-input placeholder="请输入骑手电话" v-decorator="['riderPhone', {}]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="用户优惠券id">
              <a-input placeholder="请输入用户优惠券id" v-decorator="['userCouponsId', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="是否需要发票(0:不需要 1:需要)">
              <a-input placeholder="请输入是否需要发票(0:不需要 1:需要)" v-decorator="['invoiceFlag', {}]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="发票id">
              <a-input placeholder="请输入发票id" v-decorator="['invoiceId', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="订单来源(0:微信1:安卓app 2:苹果app 3:)">
              <a-input placeholder="请输入订单来源(0:微信1:安卓app 2:苹果app 3:)" v-decorator="['orderSrc', {}]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="发票是否已开">
              <a-input placeholder="请输入发票是否已开" v-decorator="['invoiceOpen', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="订单备注">
              <a-input placeholder="请输入订单备注" v-decorator="['orderRemark', {}]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="骑手取餐时间">
              <a-date-picker
                placeholder="请输入骑手取餐时间"
                style="width:100%"
                :showTime="true"
                format="YYYY-MM-DD HH:mm:ss"
                v-decorator="[ 'getTime', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="骑手送达时间">
              <a-date-picker
                placeholder="请输入骑手送达时间"
                style="width:100%"
                :showTime="true"
                format="YYYY-MM-DD HH:mm:ss"
                v-decorator="[ 'overTime', {}]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="最小配送金额">
              <a-input-number placeholder="请输入最小配送金额" style="width:100%" v-decorator="[ 'createMinMoney', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="配送费">
              <a-input-number placeholder="请输入配送费" style="width:100%" v-decorator="[ 'deliveryMoney', {}]"/>
            </a-form-item>
          </a-col>
          <a-col :span="12" :gutter="8">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="订单状态：-1已取消 0全部；1待付款；2待发货；3待收货；4待评价；5已完成（已评价）；6售后处理中（退款&退货）；7售后已完成（退款&退货）；8已取消">
              <a-input placeholder="请输入订单状态：-1已取消 0全部；1待付款；2待发货；3待收货；4待评价；5已完成（已评价）；6售后处理中（退款&退货）；7售后已完成（退款&退货）；8已取消" v-decorator="['status', {}]"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="订单详情" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="orderGoodsTable.loading"
            :columns="orderGoodsTable.columns"
            :dataSource="orderGoodsTable.dataSource"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="true"/>
        </a-tab-pane>
      </a-tabs>

    </a-spin>
  </a-modal>
</template>

<script>

  import moment from 'moment'
  import pick from 'lodash.pick'
  import { FormTypes } from '@/utils/JEditableTableUtil'
  import { JEditableTableOneToManyMixin } from '@/mixins/JEditableTableOneToManyMixin'

  export default {
    name: 'OrderModal',
    mixins: [JEditableTableOneToManyMixin],
    data() {
      return {
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
        },
        refKeys: ['orderGoods', ],
        activeKey: 'orderGoods',
        // 订单详情
        orderGoodsTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '用户id',
              key: 'userId',
              type: FormTypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
            },
            {
              title: '商品id',
              key: 'goodsId',
              type: FormTypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
            },
            {
              title: 'orderId',
              key: 'orderId',
              type: FormTypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
            },
            {
              title: '门店id',
              key: 'storeId',
              type: FormTypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
            },
            {
              title: '订单状态：0全部；1待付款；2待发货；3待收货；4待评价；5已完成（已评价）；6售后处理中（退款&退货）；7售后已完成（退款&退货）；8已取消',
              key: 'status',
              type: FormTypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
            },
            {
              title: '商品数量',
              key: 'goodsCount',
              type: FormTypes.inputNumber,
              defaultValue: '',
              placeholder: '请输入${title}',
            },
            {
              title: '规格',
              key: 'specValue',
              type: FormTypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
            },
            {
              title: '温度',
              key: 'temperatureValue',
              type: FormTypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
            },
            {
              title: '糖度',
              key: 'sugarValue',
              type: FormTypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
            },
            {
              title: '商品单价',
              key: 'perPrice',
              type: FormTypes.inputNumber,
              defaultValue: '',
              placeholder: '请输入${title}',
            },
            {
              title: '总金额',
              key: 'totalPrice',
              type: FormTypes.inputNumber,
              defaultValue: '',
              placeholder: '请输入${title}',
            },
          ]
        },
        url: {
          add: "/order/order/add",
          edit: "/order/order/edit",
          orderGoods: {
            list: '/order/order/queryOrderGoodsByMainId'
          },
        }
      }
    },
    methods: {
 
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'userId', 'riderId', 'goodsCount', 'storeId', 'orderId', 'goodsMoney', 'orderMoney', 'orderType', 'userAddress', 'usedPhone', 'riderPhone', 'userCouponsId', 'invoiceFlag', 'invoiceId', 'orderSrc', 'invoiceOpen', 'orderRemark', 'getTime', 'overTime', 'createMinMoney', 'deliveryMoney', 'status', ))
          // 时间格式化
          this.form.setFieldsValue({ getTime: this.model.getTime ? moment(this.model.getTime) : null })
          this.form.setFieldsValue({ overTime: this.model.overTime ? moment(this.model.overTime) : null })
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.orderGoods.list, params, this.orderGoodsTable)
        }
      },
 
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        //时间格式化
        main.getTime = main.getTime ? main.getTime.format('YYYY-MM-DD HH:mm:ss') : null;
        main.overTime = main.overTime ? main.overTime.format('YYYY-MM-DD HH:mm:ss') : null;
        return {
          ...main, // 展开
          orderGoodsList: allValues.tablesValue[0].values,
        }
      }
    }
  }
</script>

<style scoped>
</style>