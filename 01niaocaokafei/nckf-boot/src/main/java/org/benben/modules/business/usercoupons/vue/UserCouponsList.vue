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
            <a-form-item label="优惠券id">
              <a-input placeholder="请输入优惠券id" v-model="queryParam.couponsId"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="领取时间">
              <a-input placeholder="请输入领取时间" v-model="queryParam.getTime"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="使用时间">
              <a-input placeholder="请输入使用时间" v-model="queryParam.useTime"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="是否被使用：0-未使用  1-使用">
              <a-input placeholder="请输入是否被使用：0-未使用  1-使用" v-model="queryParam.usedFlag"></a-input>
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
    <userCoupons-modal ref="modalForm" @ok="modalFormOk"></userCoupons-modal>
  </a-card>
</template>

<script>
  import UserCouponsModal from './modules/UserCouponsModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "UserCouponsList",
    mixins:[JeecgListMixin],
    components: {
      UserCouponsModal
    },
    data () {
      return {
        description: '用户优惠券管理页面',
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
            title: '优惠券id',
            align:"center",
            dataIndex: 'couponsId'
           },
		   {
            title: '领取时间',
            align:"center",
            dataIndex: 'getTime'
           },
		   {
            title: '使用时间',
            align:"center",
            dataIndex: 'useTime'
           },
		   {
            title: '是否被使用：0-未使用  1-使用',
            align:"center",
            dataIndex: 'usedFlag'
           },
		   {
            title: '使用条件',
            align:"center",
            dataIndex: 'useCondition'
           },
		   {
            title: '优惠券名称',
            align:"center",
            dataIndex: 'couonsName'
           },
		   {
            title: '状态：-1已过期 0 未使用 1已使用',
            align:"center",
            dataIndex: 'status'
           },
		   {
            title: '是否新用户 0--否  1--是',
            align:"center",
            dataIndex: 'newuserFlag'
           },
		   {
            title: '是否所有商家通用：0-否 1-是',
            align:"center",
            dataIndex: 'commonFlag'
           },
		   {
            title: '图片路径',
            align:"center",
            dataIndex: 'imgUrl'
           },
		   {
            title: '开始使用时间',
            align:"center",
            dataIndex: 'useStartTime'
           },
		   {
            title: '过期时间',
            align:"center",
            dataIndex: 'useEndTime'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/usercoupons/userCoupons/list",
          delete: "/usercoupons/userCoupons/delete",
          deleteBatch: "/usercoupons/userCoupons/deleteBatch",
          exportXlsUrl: "usercoupons/userCoupons/exportXls",
          importExcelUrl: "usercoupons/userCoupons/importExcel",
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