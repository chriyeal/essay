<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">学习计划管理</h1>
      <p class="page-subtitle">制定、跟踪和管理您的个性化学习计划</p>
    </div>

    <!-- 统计卡片 - 添加点击事件 -->
    <el-row :gutter="20" class="stats-row" type="flex" justify="center">
      <el-col :xs="24" :sm="8" :md="8" v-for="stat in statistics" :key="stat.key">
        <div class="stat-card" :style="{ borderColor: stat.color }" @click="filterByStatus(stat.key)">
          <div class="stat-icon" :style="{ backgroundColor: stat.color }">
            <i :class="stat.icon"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-label">{{ stat.label }}</div>
            <div class="stat-label-en">{{ stat.labelEn }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 操作栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新建计划</el-button>
        <el-button type="success" icon="el-icon-magic-stick" @click="handleGenerate">智能生成</el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="getList">刷新</el-button>
        
        <!-- 计划类型切换 -->
        <div style="margin-left: 20px; display: inline-block;">
          <el-button 
            :type="planTypeFilter === 'overall' ? 'primary' : ''" 
            @click="setPlanType('overall')">多日计划</el-button>
          <el-button 
            :type="planTypeFilter === 'today' ? 'primary' : ''" 
            @click="setPlanType('today')">今日计划</el-button>
          <el-button 
            :type="planTypeFilter === 'all' ? 'primary' : ''" 
            @click="setPlanType('all')">全部计划</el-button>
        </div>
      </div>
      
      <div class="toolbar-right">
        <el-input
          v-model="queryParams.planName"
          placeholder="搜索计划名称..."
          clearable
          style="width: 200px; margin-right: 10px;"
          @keyup.enter.native="handleQuery"
        />
        <el-select
          v-model="queryParams.status"
          placeholder="状态筛选"
          clearable
          style="width: 120px; margin-right: 10px;"
          @change="handleQuery"
        >
          <el-option label="进行中" value="0"></el-option>
          <el-option label="已完成" value="1"></el-option>
          <el-option label="已取消" value="2"></el-option>
        </el-select>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
      </div>
    </div>

    <!-- 计划列表 -->
    <el-table
      v-loading="loading"
      :data="planList"
      row-key="planId"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
      class="plan-table"
    >
      <el-table-column prop="planName" label="计划标题" min-width="200">
        <template slot-scope="scope">
          <div class="plan-title-cell">
            <i :class="getPriorityIcon(scope.row.priority)" :style="{ color: getPriorityColor(scope.row.priority) }"></i>
            <span class="plan-title">{{ scope.row.planName }}</span>
            <el-tag v-if="scope.row.isTemplate" size="mini" type="info">模板</el-tag>
          </div>
        </template>
      </el-table-column>
      
      <el-table-column prop="subject" label="学科" width="120">
        <template slot-scope="scope">
          <el-tag :type="getSubjectType(scope.row.subject)">{{ scope.row.subject }}</el-tag>
        </template>
      </el-table-column>
      
      <el-table-column prop="difficulty" label="难度" width="100">
        <template slot-scope="scope">
          <el-rate
            v-model="scope.row.difficulty"
            disabled
            show-score
            text-color="#ff9900"
            score-template="{value}"
            :max="3"
          ></el-rate>
        </template>
      </el-table-column>
      
      <el-table-column prop="startDate" label="开始时间" width="120">
        <template slot-scope="scope">
          {{ scope.row.startDate ? formatDate(scope.row.startDate) : '-' }}
        </template>
      </el-table-column>
      
      <el-table-column prop="endDate" label="结束时间" width="120">
        <template slot-scope="scope">
          {{ scope.row.endDate ? formatDate(scope.row.endDate) : '-' }}
        </template>
      </el-table-column>
      
      <el-table-column prop="progress" label="进度" width="150">
        <template slot-scope="scope">
          <el-progress
            :percentage="Math.min(100, Math.max(0, scope.row.progress || 0))"
            :status="getProgressStatus(scope.row.progress)"
            :stroke-width="10"
            text-inside
          ></el-progress>
        </template>
      </el-table-column>
      
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            :disabled="scope.row.status === '1'"
          >编辑</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleComplete(scope.row)"
            :disabled="scope.row.status === '1' || scope.row.status === '2'"
          >完成</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            :disabled="scope.row.status === '1'"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改计划对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="计划类型" prop="planType">
              <el-select v-model="form.planType" placeholder="请选择计划类型" style="width: 100%;" @change="handlePlanTypeChange">
                <el-option label="多日计划" value="overall"></el-option>
                <el-option label="今日计划" value="today"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划名称" prop="planName">
              <el-input v-model="form.planName" placeholder="请输入计划名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="学科" prop="subject">
              <el-select v-model="form.subject" placeholder="请选择学科" style="width: 100%;">
                <el-option label="数学" value="数学"></el-option>
                <el-option label="英语" value="英语"></el-option>
                <el-option label="语文" value="语文"></el-option>
                <el-option label="物理" value="物理"></el-option>
                <el-option label="化学" value="化学"></el-option>
                <el-option label="生物" value="生物"></el-option>
                <el-option label="历史" value="历史"></el-option>
                <el-option label="地理" value="地理"></el-option>
                <el-option label="政治" value="政治"></el-option>
                <el-option label="计算机" value="计算机"></el-option>
                <el-option label="其他" value="其他"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 多日计划才显示天数选择 -->
        <el-row v-if="form.planType === 'overall'">
          <el-col :span="24">
            <el-form-item label="计划天数" prop="totalDays">
              <el-input-number
                v-model="form.totalDays"
                :min="1"
                :max="maxDays"
                controls-position="right"
                style="width: 100%;"
              ></el-input-number>
              <span style="margin-left: 10px; color: #999;">（自动计算：{{ calculatedDays }} 天，可改少不可改多）</span>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="form.priority" placeholder="请选择优先级" style="width: 100%;">
                <el-option label="低" value="0"></el-option>
                <el-option label="中" value="1"></el-option>
                <el-option label="高" value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="难度等级" prop="difficulty">
              <el-rate
                v-model="form.difficulty"
                :max="3"
                show-text
                :texts="['简单', '中等', '困难']"
              ></el-rate>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 多日计划才显示日期选择 -->
        <el-row v-if="form.planType === 'overall'">
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startDate">
              <el-date-picker
                v-model="form.startDate"
                type="date"
                placeholder="选择开始时间"
                value-format="yyyy-MM-dd"
                style="width: 100%;"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endDate">
              <el-date-picker
                v-model="form.endDate"
                type="date"
                placeholder="选择结束时间"
                value-format="yyyy-MM-dd"
                style="width: 100%;"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="计划描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入计划描述"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="学习目标">
          <el-input
            v-model="form.learningGoals"
            type="textarea"
            :rows="2"
            placeholder="请输入学习目标，多个目标用分号分隔"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="作为模板">
          <el-switch
            v-model="form.isTemplate"
            active-text="是"
            inactive-text="否"
          ></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 智能生成对话框 -->
    <el-dialog title="智能生成学习计划" :visible.sync="generateOpen" width="600px" append-to-body>
      <el-form ref="generateForm" :model="generateForm" :rules="generateRules" label-width="120px">
        <el-form-item label="学习主题" prop="topic">
          <el-input v-model="generateForm.topic" placeholder="请输入学习主题" />
        </el-form-item>
        
        <el-form-item label="预计学习时长" prop="duration">
          <el-input-number
            v-model="generateForm.duration"
            :min="1"
            :max="365"
            controls-position="right"
          ></el-input-number>
          <span style="margin-left: 10px;">天</span>
        </el-form-item>
        
        <el-form-item label="每日学习时间" prop="dailyHours">
          <el-input-number
            v-model="generateForm.dailyHours"
            :min="0.5"
            :max="24"
            :step="0.5"
            controls-position="right"
          ></el-input-number>
          <span style="margin-left: 10px;">小时</span>
        </el-form-item>
        
        <el-form-item label="学科领域" prop="subject">
          <el-select v-model="generateForm.subject" placeholder="请选择学科" style="width: 100%;">
            <el-option label="数学" value="数学"></el-option>
            <el-option label="英语" value="英语"></el-option>
            <el-option label="语文" value="语文"></el-option>
            <el-option label="物理" value="物理"></el-option>
            <el-option label="化学" value="化学"></el-option>
            <el-option label="生物" value="生物"></el-option>
            <el-option label="历史" value="历史"></el-option>
            <el-option label="地理" value="地理"></el-option>
            <el-option label="政治" value="政治"></el-option>
            <el-option label="计算机" value="计算机"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="难度等级">
          <el-rate
            v-model="generateForm.difficulty"
            :max="3"
            show-text
            :texts="['简单', '中等', '困难']"
          ></el-rate>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitGenerate" :loading="generateLoading">生成计划</el-button>
        <el-button @click="cancelGenerate">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listStudyPlan, getStudyPlan, delStudyPlan, addStudyPlan, updateStudyPlan, completeStudyPlan, getPlanStatistics, getPlanSummary, generateSmartPlan } from "@/api/study/plan";

export default {
  name: "StudyPlan",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 学习计划表格数据
      planList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示生成弹出层
      generateOpen: false,
      // 生成加载状态
      generateLoading: false,
      // 统计数据
      statistics: [
        { key: 'total', label: '总计划数', labelEn: 'Total Plans', value: 0, icon: 'el-icon-notebook-2', color: '#409EFF' },
        { key: 'ongoing', label: '进行中', labelEn: 'Ongoing', value: 0, icon: 'el-icon-loading', color: '#67C23A' },
        { key: 'completed', label: '已完成', labelEn: 'Completed', value: 0, icon: 'el-icon-check', color: '#E6A23C' }
      ],
      // 计划类型筛选
      planTypeFilter: 'all',
      // 最大天数限制
      maxDays: 365,
      // 计算出的天数
      calculatedDays: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        planName: null,
        subject: null,
        status: null
      },
      // 表单参数
      form: {},
      // 生成表单参数
      generateForm: {
        topic: '',
        duration: 30,
        dailyHours: 2,
        subject: '',
        difficulty: 2
      },
      // 表单校验
      rules: {
        planName: [
          { required: true, message: "计划名称不能为空", trigger: "blur" }
        ],
        subject: [
          { required: true, message: "学科不能为空", trigger: "change" }
        ],
        priority: [
          { required: true, message: "优先级不能为空", trigger: "change" }
        ],
        startDate: [
          { required: true, message: "开始时间不能为空", trigger: "change" }
        ],
        endDate: [
          { required: true, message: "结束时间不能为空", trigger: "change" }
        ]
      },
      // 生成表单校验
      generateRules: {
        topic: [
          { required: true, message: "学习主题不能为空", trigger: "blur" }
        ],
        duration: [
          { required: true, message: "预计学习时长不能为空", trigger: "change" }
        ],
        dailyHours: [
          { required: true, message: "每日学习时间不能为空", trigger: "change" }
        ],
        subject: [
          { required: true, message: "学科不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    // 初始化查询参数与筛选状态一致
    this.queryParams.planType = null; // 'all' 对应 null
    this.getList();
    this.getStatistics();
  },
  watch: {
    // 监听日期变化自动计算天数
    'form.startDate': function() {
      this.calculateDays();
    },
    'form.endDate': function() {
      this.calculateDays();
    }
  },
  methods: {
    /** 查询学习计划列表 */
    getList() {
      console.log('===== getList() 被调用 =====');
      console.log('当前 queryParams:', JSON.stringify(this.queryParams));
      this.loading = true;
      listStudyPlan(this.queryParams).then(response => {
        console.log('后端返回数据:', response);
        this.planList = response.rows;
        this.total = response.total;
        console.log('列表已更新，记录数:', response.rows.length);
        this.loading = false;
      }).catch(error => {
        console.error('查询失败:', error);
        this.loading = false;
      });
    },
    /** 查询统计信息 */
    getStatistics() {
      console.log('开始获取统计数据...');
      getPlanSummary().then(response => {
        console.log('统计数据响应:', response.data);
        const stats = response.data || {};
        this.statistics[0].value = stats.totalPlans || 0;
        this.statistics[1].value = stats.ongoingPlans || 0;
        this.statistics[2].value = stats.completedPlans || 0;
        console.log('统计数据已更新:', this.statistics);
      }).catch(error => {
        console.error('获取统计数据失败:', error);
      });
    },
    /** 根据状态筛选计划 */
    filterByStatus(statusKey) {
      const statusMap = {
        'total': null,
        'ongoing': '0',
        'completed': '1',
        'templates': null
      };
      
      this.queryParams.status = statusMap[statusKey] || null;
      if (statusKey === 'templates') {
        this.queryParams.isTemplate = 1;
      } else {
        this.queryParams.isTemplate = null;
      }
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 计划类型改变 */
    setPlanType(type) {
      console.log('===== 计划类型切换 =====');
      console.log('点击的类型:', type);
      this.planTypeFilter = type;
      // 清空之前的筛选条件
      this.queryParams.status = null;
      this.queryParams.isTemplate = null;
      
      if (type === 'overall') {
        // 多日计划：未完成的计划
        this.queryParams.planType = 'overall';
        console.log('设置 planType 为 overall');
      } else if (type === 'today') {
        // 今日计划：今天的计划
        this.queryParams.planType = 'today';
        console.log('设置 planType 为 today');
      } else {
        // 全部计划：包括已完成和未完成的所有计划
        this.queryParams.planType = null;
        console.log('设置 planType 为 null');
      }
      
      this.queryParams.pageNum = 1;
      console.log('最终查询参数:', JSON.stringify(this.queryParams));
      console.log('开始调用 getList()');
      this.getList();
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 取消生成
    cancelGenerate() {
      this.generateOpen = false;
      this.resetGenerateForm();
    },
    // 表单重置
    reset() {
      this.form = {
        planId: null,
        planName: null,
        planType: 'overall',  // 默认为多日计划
        subject: null,
        priority: "1",
        difficulty: 2,
        startDate: null,
        endDate: null,
        description: null,
        learningGoals: null,
        totalDays: 0,
        isTemplate: 0
      };
      this.calculatedDays = 0;
      this.resetForm("form");
    },
    // 生成表单重置
    resetGenerateForm() {
      this.generateForm = {
        topic: '',
        duration: 30,
        dailyHours: 2,
        subject: '',
        difficulty: 2
      };
      this.resetForm("generateForm");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.planId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加学习计划";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const planId = row.planId || this.ids;
      getStudyPlan(planId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改学习计划";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 确保 isTemplate 是数字类型
          const formData = { ...this.form };
          formData.isTemplate = this.form.isTemplate ? 1 : 0;
          
          if (this.form.planId != null) {
            updateStudyPlan(formData).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
              this.getStatistics();
            });
          } else {
            addStudyPlan(formData).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
              this.getStatistics();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const planIds = row.planId || this.ids;
      this.$modal.confirm('是否确认删除学习计划编号为"' + planIds + '"的数据项？').then(function() {
        return delStudyPlan(planIds);
      }).then(() => {
        this.getList();
        this.getStatistics();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 完成按钮操作 */
    handleComplete(row) {
      this.$modal.confirm('是否确认完成该学习计划？').then(() => {
        return completeStudyPlan(row.planId);
      }).then(() => {
        this.getList();
        this.getStatistics();
        this.$modal.msgSuccess("计划已完成");
      }).catch(() => {});
    },
    /** 查看按钮操作 */
    handleView(row) {
      // 显示计划详情
      const h = this.$createElement;
      const content = h('div', { style: { 'font-size': '14px', 'line-height': '1.8' } }, [
        h('el-descriptions', { props: { title: '基本信息', column: 2, border: true } }, [
          h('el-descriptions-item', { props: { label: '计划名称' } }, row.planName),
          h('el-descriptions-item', { props: { label: '学科' } }, row.subject || '-'),
          h('el-descriptions-item', { props: { label: '优先级' } }, this.getPriorityText(row.priority)),
          h('el-descriptions-item', { props: { label: '难度' } }, this.getDifficultyText(row.difficulty)),
          h('el-descriptions-item', { props: { label: '开始时间' } }, row.startDate ? this.formatDate(row.startDate) : '-'),
          h('el-descriptions-item', { props: { label: '结束时间' } }, row.endDate ? this.formatDate(row.endDate) : '-'),
          h('el-descriptions-item', { props: { label: '进度' } }, row.progress + '%'),
          h('el-descriptions-item', { props: { label: '状态' } }, this.getStatusTag(row.status)),
          h('el-descriptions-item', { props: { label: '描述', span: 2 } }, row.description || '无')
        ])
      ]);
      
      this.$alert(content, '计划详情', {
        confirmButtonText: '关闭',
        dangerouslyUseHTMLString: true,
        customClass: 'plan-detail-dialog'
      });
    },
    /** 智能生成按钮操作 */
    handleGenerate() {
      // 直接调用后端接口，基于优先级和截止日期自动生成
      this.loading = true;
      generateSmartPlan({}).then(response => {
        const plans = response.data || [];
        if (plans.length > 0) {
          this.$modal.msgSuccess('已生成' + plans.length + '个学习计划');
          // 切换到今日计划标签
          this.planTypeFilter = 'today';
          this.handlePlanTypeChange();
          this.getList();
          this.getStatistics();
        } else {
          this.$modal.msgWarning('没有可生成的学习计划，请先添加总体计划');
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    /** 处理计划类型变化 */
    handlePlanTypeChange() {
      // 如果是今日计划，设置为今天的日期
      if (this.form.planType === 'today') {
        const today = new Date();
        const dateStr = today.getFullYear() + '-' + 
                       String(today.getMonth() + 1).padStart(2, '0') + '-' + 
                       String(today.getDate()).padStart(2, '0');
        this.form.startDate = dateStr;
        this.form.endDate = dateStr;
        this.form.totalDays = 1;
        this.calculatedDays = 1;
      } else {
        // 多日计划，清空日期
        this.form.startDate = null;
        this.form.endDate = null;
        this.form.totalDays = 0;
        this.calculatedDays = 0;
      }
    },
    /** 计算天数 */
    calculateDays() {
      if (this.form.startDate && this.form.endDate) {
        const start = new Date(this.form.startDate);
        const end = new Date(this.form.endDate);
        const diffTime = Math.abs(end - start);
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1; // 包含首尾两天
        this.calculatedDays = diffDays;
        
        // 如果 totalDays 为 0 或大于计算天数，则设置为计算天数
        if (!this.form.totalDays || this.form.totalDays > diffDays) {
          this.form.totalDays = diffDays;
        }
        
        // 设置 maxDays 为计算天数（只能改少不能改多）
        this.maxDays = diffDays;
      }
    },
    /** 提交生成 */
    submitGenerate() {
      this.$refs["generateForm"].validate(valid => {
        if (valid) {
          this.generateLoading = true;
          generateSmartPlan(this.generateForm).then(response => {
            this.$modal.msgSuccess("智能计划生成成功");
            this.generateOpen = false;
            this.getList();
            this.getStatistics();
          }).finally(() => {
            this.generateLoading = false;
          });
        }
      });
    },
    // 格式化日期
    formatDate(date) {
      if (!date) return '-';
      return date.split(' ')[0];
    },
    // 获取优先级图标
    getPriorityIcon(priority) {
      const icons = {
        '0': 'el-icon-arrow-down',
        '1': 'el-icon-minus',
        '2': 'el-icon-arrow-up'
      };
      return icons[priority] || 'el-icon-minus';
    },
    // 获取优先级文本
    getPriorityText(priority) {
      const texts = {
        '0': '低优先级',
        '1': '中优先级',
        '2': '高优先级'
      };
      return texts[priority] || '中优先级';
    },
    // 获取难度文本
    getDifficultyText(difficulty) {
      const texts = {
        '1': '简单',
        '2': '中等',
        '3': '困难'
      };
      return texts[difficulty] || '中等';
    },
    // 获取状态标签
    getStatusTag(status) {
      const h = this.$createElement;
      const types = {
        '0': 'primary',
        '1': 'success',
        '2': 'info'
      };
      const texts = {
        '0': '进行中',
        '1': '已完成',
        '2': '已取消'
      };
      return h('el-tag', { props: { type: types[status] || 'info', size: 'small' } }, texts[status] || '未知');
    },
    // 格式化时间
    formatDate(dateStr) {
      if (!dateStr) return '-';
      const date = new Date(dateStr);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    // 获取优先级颜色
    getPriorityColor(priority) {
      const colors = {
        '0': '#909399',
        '1': '#409EFF',
        '2': '#F56C6C'
      };
      return colors[priority] || '#409EFF';
    },
    // 获取学科标签类型
    getSubjectType(subject) {
      const types = {
        '数学': 'success',
        '英语': 'primary',
        '语文': 'warning',
        '物理': 'danger',
        '化学': '',
        '生物': 'info',
        '历史': 'warning',
        '地理': 'success',
        '政治': 'primary',
        '计算机': 'danger'
      };
      return types[subject] || '';
    },
    // 获取进度状态
    getProgressStatus(progress) {
      if (progress >= 100) return 'success';
      if (progress >= 50) return 'warning';
      return 'exception';
    },
    // 获取状态类型
    getStatusType(status) {
      const types = {
        '0': 'primary',  // 进行中
        '1': 'success',  // 已完成
        '2': 'info'      // 已取消
      };
      return types[status] || 'info';
    },
    // 获取状态文本
    getStatusText(status) {
      const texts = {
        '0': '进行中',
        '1': '已完成',
        '2': '已取消'
      };
      return texts[status] || '未知';
    }
  }
};
</script>

<style scoped lang="scss">
.app-container {
  padding: 20px;
  
  .page-header {
    margin-bottom: 30px;
    text-align: center;
    
    .page-title {
      font-size: 2rem;
      font-weight: 300;
      color: #333;
      margin-bottom: 10px;
    }
    
    .page-subtitle {
      font-size: 1.1rem;
      color: #666;
      font-weight: 300;
    }
  }
  
  .stats-row {
    margin-bottom: 30px;
    
    .stat-card {
      background: white;
      border-radius: 12px;
      padding: 25px;
      text-align: center;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      border: 1px solid #f0f0f0;
      transition: all 0.3s ease;
      height: 100%;
      
      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
      }
      
      .stat-icon {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 15px;
        color: white;
        font-size: 24px;
      }
      
      .stat-content {
        .stat-value {
          font-size: 2rem;
          font-weight: 600;
          color: #333;
          margin-bottom: 8px;
        }
        
        .stat-label {
          font-size: 1.1rem;
          color: #666;
          margin-bottom: 4px;
        }
        
        .stat-label-en {
          font-size: 0.9rem;
          color: #999;
          font-weight: 300;
        }
      }
    }
  }
  
  .toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    flex-wrap: wrap;
    gap: 10px;
    
    .toolbar-left {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      gap: 10px;
    }
    
    .toolbar-right {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      gap: 10px;
    }
  }
  
  .plan-table {
    .plan-title-cell {
      display: flex;
      align-items: center;
      gap: 10px;
      
      .plan-title {
        font-weight: 500;
        color: #333;
      }
    }
    
    .progress-text {
      font-size: 0.85rem;
      color: #999;
      margin-top: 5px;
      text-align: center;
    }
  }
  
  // 响应式设计
  @media (max-width: 768px) {
    .page-header .page-title {
      font-size: 1.5rem;
    }
    
    .toolbar {
      flex-direction: column;
      align-items: stretch;
      
      .toolbar-right {
        justify-content: center;
      }
    }
  }
}
</style>