<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :span="6">
            <a-form-item label="用户id">
              <a-input placeholder="请输入用户id" v-model="queryParam.userId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="骑手id">
              <a-input placeholder="请输入骑手id" v-model="queryParam.riderId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>
        <span>已选择</span>
        <a style="font-weight: 600">
          {{ selectedRowKeys.length }}
        </a>
        <span>项</span>
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <order-modal ref="modalForm" @ok="modalFormOk"/>

  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import OrderModal from './modules/OrderModal'

  export default {
    name: "OrderList",
    mixins: [JeecgListMixin],
    components: {
      OrderModal
    },
    data () {
      return {
        description: '订单管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender:function (t, r, index) {
              return parseInt(index)+1;
            }
          },
          {
            title: '用户id',
            align:"center",
            dataIndex: 'userId'
          },
          {
            title: '骑手id',
            align:"center",
            dataIndex: 'riderId'
          },
          {
            title: '商品数量',
            align:"center",
            dataIndex: 'goodsCount'
          },
          {
            title: '门店id',
            align:"center",
            dataIndex: 'storeId'
          },
          {
            title: '订单编号',
            align:"center",
            dataIndex: 'orderId'
          },
          {
            title: '商品总金额',
            align:"center",
            dataIndex: 'goodsMoney'
          },
          {
            title: '订单总金额',
            align:"center",
            dataIndex: 'orderMoney'
          },
          {
            title: '订单类型(0:送餐  1：店内用餐)',
            align:"center",
            dataIndex: 'orderType'
          },
          {
            title: '送餐地址',
            align:"center",
            dataIndex: 'userAddress'
          },
          {
            title: '用户电话',
            align:"center",
            dataIndex: 'usedPhone'
          },
          {
            title: '骑手电话',
            align:"center",
            dataIndex: 'riderPhone'
          },
          {
            title: '用户优惠券id',
            align:"center",
            dataIndex: 'userCouponsId'
          },
          {
            title: '是否需要发票(0:不需要 1:需要)',
            align:"center",
            dataIndex: 'invoiceFlag'
          },
          {
            title: '发票id',
            align:"center",
            dataIndex: 'invoiceId'
          },
          {
            title: '订单来源(0:微信1:安卓app 2:苹果app 3:)',
            align:"center",
            dataIndex: 'orderSrc'
          },
          {
            title: '发票是否已开',
            align:"center",
            dataIndex: 'invoiceOpen'
          },
          {
            title: '订单备注',
            align:"center",
            dataIndex: 'orderRemark'
          },
          {
            title: '骑手取餐时间',
            align:"center",
            dataIndex: 'getTime'
          },
          {
            title: '骑手送达时间',
            align:"center",
            dataIndex: 'overTime'
          },
          {
            title: '最小配送金额',
            align:"center",
            dataIndex: 'createMinMoney'
          },
          {
            title: '配送费',
            align:"center",
            dataIndex: 'deliveryMoney'
          },
          {
            title: '订单状态：-1已取消 0全部；1待付款；2待发货；3待收货；4待评价；5已完成（已评价）；6售后处理中（退款&退货）；7售后已完成（退款&退货）；8已取消',
            align:"center",
            dataIndex: 'status'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        // 请求参数
    	url: {
              list: "/order/order/list",
              delete: "/order/order/delete",
              deleteBatch: "/order/order/deleteBatch",
              exportXlsUrl: "order/order/exportXls",
              importExcelUrl: "order/order/importExcel",
           },
        }
      },
      computed: {
        importExcelUrl: function(){
          return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
        }
      },


    methods: {

      initDictConfig() {
      }

    }
  }
</script>
<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 3px
  }
  .ant-card-body .table-operator {
    margin-bottom: 18px;
  }

  .ant-table-tbody .ant-table-row td {
    padding-top: 15px;
    padding-bottom: 15px;
  }

  .anty-row-operator button {
    margin: 0 5px
  }

  .ant-btn-danger {
    background-color: #ffffff
  }

  .ant-modal-cust-warp {
    height: 100%
  }

  .ant-modal-cust-warp .ant-modal-body {
    height: calc(100% - 110px) !important;
    overflow-y: auto
  }

  .ant-modal-cust-warp .ant-modal-content {
    height: 90% !important;
    overflow-y: hidden
  }
</style>