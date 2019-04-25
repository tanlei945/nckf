<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="用户id">
              <a-input placeholder="请输入用户id" v-model="queryParam.userId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="纳税人名称">
              <a-input placeholder="请输入纳税人名称" v-model="queryParam.taxName"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="税号">
              <a-input placeholder="请输入税号" v-model="queryParam.taxNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="发票类型（个人:0  公司:1）">
              <a-input placeholder="请输入发票类型（个人:0  公司:1）" v-model="queryParam.invoiceType"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="开户行名称">
              <a-input placeholder="请输入开户行名称" v-model="queryParam.companyBank"></a-input>
            </a-form-item>
          </a-col>
        </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
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
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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
    <invoiceTitle-modal ref="modalForm" @ok="modalFormOk"></invoiceTitle-modal>
  </a-card>
</template>

<script>
  import InvoiceTitleModal from './modules/InvoiceTitleModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "InvoiceTitleList",
    mixins:[JeecgListMixin],
    components: {
      InvoiceTitleModal
    },
    data () {
      return {
        description: '用户发票抬头管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
           },
		   {
            title: '用户id',
            align:"center",
            dataIndex: 'userId'
           },
		   {
            title: '纳税人名称',
            align:"center",
            dataIndex: 'taxName'
           },
		   {
            title: '税号',
            align:"center",
            dataIndex: 'taxNo'
           },
		   {
            title: '发票类型（个人:0  公司:1）',
            align:"center",
            dataIndex: 'invoiceType'
           },
		   {
            title: '开户行名称',
            align:"center",
            dataIndex: 'companyBank'
           },
		   {
            title: '公司地址',
            align:"center",
            dataIndex: 'companyAddress'
           },
		   {
            title: '银行账号',
            align:"center",
            dataIndex: 'bankAccount'
           },
		   {
            title: '电话号码',
            align:"center",
            dataIndex: 'telephone'
           },
		   {
            title: '0：失败 1 成功',
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
		url: {
          list: "/invoice/invoiceTitle/list",
          delete: "/invoice/invoiceTitle/delete",
          deleteBatch: "/invoice/invoiceTitle/deleteBatch",
          exportXlsUrl: "invoice/invoiceTitle/exportXls",
          importExcelUrl: "invoice/invoiceTitle/importExcel",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
     
    }
  }
</script>
<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 3px
  }
  .ant-card-body .table-operator{
    margin-bottom: 18px;
  }
  .ant-table-tbody .ant-table-row td{
    padding-top:15px;
    padding-bottom:15px;
  }
  .anty-row-operator button{margin: 0 5px}
  .ant-btn-danger{background-color: #ffffff}

  .ant-modal-cust-warp{height: 100%}
  .ant-modal-cust-warp .ant-modal-body{height:calc(100% - 110px) !important;overflow-y: auto}
  .ant-modal-cust-warp .ant-modal-content{height:90% !important;overflow-y: hidden}
</style>