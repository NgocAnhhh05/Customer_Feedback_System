import React, { useState, useEffect, useRef } from 'react';
import { 
  RoleType, 
  FeedbackStatus, 
  FeedbackTopic, 
  User, 
  Feedback, 
  Attachment, 
  Reply 
} from './types.ts';
import { 
  MessageSquare, 
  Star, 
  Shield, 
  Users, 
  BarChart3, 
  LogOut, 
  Plus, 
  Image as ImageIcon, 
  Trash2, 
  Filter, 
  Search, 
  Send, 
  FileText, 
  CheckCircle, 
  AlertCircle, 
  Sparkles, 
  Download, 
  Calendar, 
  UserPlus, 
  Clock, 
  Check, 
  UserCheck,
  Building2,
  FolderLock,
  Upload,
  Paperclip
} from 'lucide-react';

// Pre-defined mockup images for image attachment selections
const MOCK_ATTACHMENT_IMAGES = [
  { name: 'error_screenshot.png', url: 'https://images.unsplash.com/photo-1531403009284-440f080d1e12?w=300&q=80', size: 245000 },
  { name: 'broken_ui.png', url: 'https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?w=300&q=80', size: 104000 },
  { name: 'checkout_fail.jpg', url: 'https://images.unsplash.com/photo-1557200134-90327ee9fafa?w=300&q=80', size: 189000 },
];

export default function App() {
  // --- STATE ---
  const [users, setUsers] = useState<User[]>([]);
  const [feedbacks, setFeedbacks] = useState<Feedback[]>([]);
  const [currentUser, setCurrentUser] = useState<User | null>(null);
  
  // Auth Form State
  const [loginEmail, setLoginEmail] = useState('');
  const [loginPassword, setLoginPassword] = useState('');
  const [loginError, setLoginError] = useState('');
  const [showRegister, setShowRegister] = useState(false);
  
  // Register Form State
  const [regName, setRegName] = useState('');
  const [regEmail, setRegEmail] = useState('');
  const [regPhone, setRegPhone] = useState('');
  const [regPassword, setRegPassword] = useState('');
  const [regRole, setRegRole] = useState<RoleType>(RoleType.CUSTOMER);
  const [regDept, setRegDept] = useState('');
  const [regSpec, setRegSpec] = useState('');
  const [regError, setRegError] = useState('');

  // Submit Feedback State (Customer)
  const [newFeedbackContent, setNewFeedbackContent] = useState('');
  const [newFeedbackRating, setNewFeedbackRating] = useState(5);
  const [newFeedbackTopic, setNewFeedbackTopic] = useState<FeedbackTopic>(FeedbackTopic.PRODUCT_ISSUE);
  const [selectedMockImages, setSelectedMockImages] = useState<typeof MOCK_ATTACHMENT_IMAGES>([]);
  const [uploadedFiles, setUploadedFiles] = useState<{ name: string; url: string; size: number }[]>([]);
  const [dragActive, setDragActive] = useState(false);
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [customFileError, setCustomFileError] = useState('');
  const [submitSuccess, setSubmitSuccess] = useState(false);

  // Customer Filters
  const [customerStatusFilter, setCustomerStatusFilter] = useState<'ALL' | FeedbackStatus>('ALL');

  // Admin Search & Filters
  const [adminSearch, setAdminSearch] = useState('');
  const [adminTopicFilter, setAdminTopicFilter] = useState<'ALL' | FeedbackTopic>('ALL');
  const [adminStatusFilter, setAdminStatusFilter] = useState<'ALL' | FeedbackStatus>('ALL');
  const [selectedAdminFeedback, setSelectedAdminFeedback] = useState<Feedback | null>(null);
  const [adminReplyText, setAdminReplyText] = useState('');
  const [adminReplyError, setAdminReplyError] = useState('');

  // Manager - Analytics State
  const [managerStartDate, setManagerStartDate] = useState('2026-06-01');
  const [managerEndDate, setManagerEndDate] = useState('2026-06-30');
  const [exportingReport, setExportingReport] = useState(false);
  const [exportSuccessMessage, setExportSuccessMessage] = useState('');

  // Manager - User Management State
  const [showAddUserModal, setShowAddUserModal] = useState(false);
  const [newUserEmail, setNewUserEmail] = useState('');
  const [newUserName, setNewUserName] = useState('');
  const [newUserRole, setNewUserRole] = useState<RoleType>(RoleType.ADMIN);
  const [newUserPassword, setNewUserPassword] = useState('');
  const [newUserPhone, setNewUserPhone] = useState('');
  const [newUserDept, setNewUserDept] = useState('CUSTOMER_SUCCESS');
  const [newUserSpec, setNewUserSpec] = useState('TECHNICAL_SUPPORT');
  const [userManageError, setUserManageError] = useState('');

  // --- INITIALIZE DATA (localStorage cache) ---
  useEffect(() => {
    const cachedUsers = localStorage.getItem('cs_users');
    const cachedFeedbacks = localStorage.getItem('cs_feedbacks');

    if (cachedUsers && cachedFeedbacks) {
      setUsers(JSON.parse(cachedUsers));
      setFeedbacks(JSON.parse(cachedFeedbacks));
    } else {
      // Seed initial users
      const initialUsers: User[] = [
        {
          id: 1,
          email: 'customer@gmail.com',
          name: 'Nguyễn Văn A',
          role: RoleType.CUSTOMER,
          isActive: true,
          phone: '0912345678'
        },
        {
          id: 2,
          email: 'admin@gmail.com',
          name: 'Trần Thị B (Admin)',
          role: RoleType.ADMIN,
          isActive: true,
          phone: '0987654321',
          specialization: 'TECHNICAL_SUPPORT'
        },
        {
          id: 3,
          email: 'manager@gmail.com',
          name: 'Lê Văn C (Manager)',
          role: RoleType.MANAGER,
          isActive: true,
          phone: '0909090909',
          department: 'CUSTOMER_SUCCESS'
        }
      ];

      // Seed initial feedbacks
      const initialFeedbacks: Feedback[] = [
        {
          id: 101,
          customerId: 1,
          customerName: 'Nguyễn Văn A',
          customerEmail: 'customer@gmail.com',
          content: 'Ứng dụng chạy rất tốt nhưng thời gian tải trang thanh toán hơi lâu. Đôi khi bị nghẽn khoảng 10 giây.',
          rating: 4,
          topic: FeedbackTopic.PRODUCT_ISSUE,
          status: FeedbackStatus.RESOLVED,
          createdAt: '2026-06-20T10:15:00-07:00',
          updatedAt: '2026-06-21T14:30:00-07:00',
          attachments: [
            {
              id: 501,
              fileName: 'checkout_fail.jpg',
              fileType: 'image/jpeg',
              fileSize: 189000,
              url: 'https://images.unsplash.com/photo-1557200134-90327ee9fafa?w=300&q=80'
            }
          ],
          replies: [
            {
              id: 901,
              feedbackId: 101,
              adminId: 2,
              adminName: 'Trần Thị B (Admin)',
              content: 'Chào anh A, chúng tôi đã nhận được thông tin và tiến hành nâng cấp máy chủ thanh toán tối qua. Hiện tại tốc độ đã được tối ưu hóa xuống còn dưới 1 giây. Xin lỗi anh vì trải nghiệm không tốt trước đó!',
              createdAt: '2026-06-21T14:30:00-07:00'
            }
          ]
        },
        {
          id: 102,
          customerId: 1,
          customerName: 'Nguyễn Văn A',
          customerEmail: 'customer@gmail.com',
          content: 'Nhân viên giao hàng thái độ cộc lốc, không chịu giao lên chung cư mà bắt xuống sảnh nhận hàng giữa trưa nắng.',
          rating: 2,
          topic: FeedbackTopic.DELIVERY_ISSUE,
          status: FeedbackStatus.PENDING,
          createdAt: '2026-06-23T08:00:00-07:00',
          updatedAt: '2026-06-23T08:00:00-07:00',
          attachments: [],
          replies: []
        },
        {
          id: 103,
          customerId: 1,
          customerName: 'Nguyễn Văn A',
          customerEmail: 'customer@gmail.com',
          content: 'Tôi đề xuất hệ thống nên có thêm tính năng tích điểm đổi quà cho khách hàng thân thiết.',
          rating: 5,
          topic: FeedbackTopic.SUGGESTION,
          status: FeedbackStatus.RESOLVED,
          createdAt: '2026-06-15T09:00:00-07:00',
          updatedAt: '2026-06-16T11:00:00-07:00',
          attachments: [],
          replies: [
            {
              id: 902,
              feedbackId: 103,
              adminId: 2,
              adminName: 'Trần Thị B (Admin)',
              content: 'Đóng góp tuyệt vời! Hệ thống quà tặng và tích điểm Loyalty đang được phòng Marketing phát triển và dự kiến tích hợp trong phiên bản tháng 8 tới. Cảm ơn anh rất nhiều!',
              createdAt: '2026-06-16T11:00:00-07:00'
            }
          ]
        }
      ];

      setUsers(initialUsers);
      setFeedbacks(initialFeedbacks);
      localStorage.setItem('cs_users', JSON.stringify(initialUsers));
      localStorage.setItem('cs_feedbacks', JSON.stringify(initialFeedbacks));
    }
  }, []);

  // Save changes to localStorage whenever state updates
  const saveState = (updatedUsers: User[], updatedFeedbacks: Feedback[]) => {
    setUsers(updatedUsers);
    setFeedbacks(updatedFeedbacks);
    localStorage.setItem('cs_users', JSON.stringify(updatedUsers));
    localStorage.setItem('cs_feedbacks', JSON.stringify(updatedFeedbacks));
  };

  // --- ACTIONS ---

  // UC01: Login Action
  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    setLoginError('');

    if (!loginEmail || !loginPassword) {
      setLoginError('Vui lòng nhập đầy đủ email và mật khẩu.');
      return;
    }

    const matchedUser = users.find(u => u.email.toLowerCase() === loginEmail.toLowerCase());
    if (matchedUser) {
      if (!matchedUser.isActive) {
        setLoginError('Tài khoản này đã bị khóa hoặc ngừng hoạt động.');
        return;
      }
      setCurrentUser(matchedUser);
      // Clean states
      setNewFeedbackContent('');
      setNewFeedbackRating(5);
      setSelectedMockImages([]);
      setSelectedAdminFeedback(null);
    } else {
      setLoginError('Email hoặc mật khẩu không chính xác.');
    }
  };



  // Register Account (All Roles Supported)
  const handleRegister = (e: React.FormEvent) => {
    e.preventDefault();
    setRegError('');

    if (!regName || !regEmail || !regPassword) {
      setRegError('Họ tên, email và mật khẩu là bắt buộc.');
      return;
    }

    // BR-04: Unique User Account
    if (users.some(u => u.email.toLowerCase() === regEmail.toLowerCase())) {
      setRegError('Email này đã được sử dụng trên hệ thống.');
      return;
    }

    const newUser: User = {
      id: Date.now(),
      email: regEmail,
      name: regName,
      role: regRole,
      isActive: true,
      phone: regPhone || undefined,
      department: regRole === RoleType.MANAGER ? (regDept.trim() || 'Ban Quản Lý Chung') : undefined,
      specialization: regRole === RoleType.ADMIN ? (regSpec.trim() || 'Hỗ Trợ Khách Hàng') : undefined
    };

    const updatedUsers = [...users, newUser];
    saveState(updatedUsers, feedbacks);
    setCurrentUser(newUser);
    setShowRegister(false);
    // Reset fields
    setRegName('');
    setRegEmail('');
    setRegPhone('');
    setRegPassword('');
    setRegRole(RoleType.CUSTOMER);
    setRegDept('');
    setRegSpec('');
  };

  const handleLogout = () => {
    setCurrentUser(null);
  };

  // UC02 & UC03: Submit Feedback with Attachments
  const handleFeedbackSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setCustomFileError('');

    if (!currentUser) return;

    // BR-07: Mandatory Feedback Content
    if (!newFeedbackContent.trim()) {
      setCustomFileError('Nội dung phản hồi không được để trống.');
      return;
    }

    // BR-08: Rating Requirement
    if (newFeedbackRating < 1 || newFeedbackRating > 5) {
      setCustomFileError('Điểm đánh giá phải từ 1 đến 5 sao.');
      return;
    }

    // Prepare attachments (UC03) - combining selected mock images & uploaded device files
    const mockAtts: Attachment[] = selectedMockImages.map((img, idx) => ({
      id: Date.now() + idx,
      fileName: img.name,
      fileType: 'image/jpeg',
      fileSize: img.size,
      url: img.url
    }));

    const uploadedAtts: Attachment[] = uploadedFiles.map((file, idx) => ({
      id: Date.now() + selectedMockImages.length + idx,
      fileName: file.name,
      fileType: 'image/jpeg',
      fileSize: file.size,
      url: file.url
    }));

    const attachments = [...mockAtts, ...uploadedAtts];

    const newFb: Feedback = {
      id: Date.now(),
      customerId: currentUser.id,
      customerName: currentUser.name,
      customerEmail: currentUser.email,
      content: newFeedbackContent,
      rating: newFeedbackRating,
      topic: newFeedbackTopic,
      status: FeedbackStatus.PENDING, // BR-11: Initial Feedback Status is PENDING
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      attachments,
      replies: []
    };

    const updatedFeedbacks = [newFb, ...feedbacks];
    saveState(users, updatedFeedbacks);
    
    // Clear submission form
    setNewFeedbackContent('');
    setNewFeedbackRating(5);
    setSelectedMockImages([]);
    setUploadedFiles([]);
    setSubmitSuccess(true);
    setTimeout(() => setSubmitSuccess(false), 4000);
  };

  // Device File Upload Handlers (for customer uploading from own device)
  const handleDeviceFiles = (files: FileList) => {
    setCustomFileError('');
    const newFiles: { name: string; url: string; size: number }[] = [];
    
    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      // BR-09: Size restriction limit (5MB)
      if (file.size > 5 * 1024 * 1024) {
        setCustomFileError(`Tệp tin "${file.name}" vượt quá kích thước cho phép (5MB).`);
        continue;
      }
      
      const fileUrl = URL.createObjectURL(file);
      newFiles.push({
        name: file.name,
        url: fileUrl,
        size: file.size
      });
    }
    
    if (newFiles.length > 0) {
      setUploadedFiles(prev => [...prev, ...newFiles]);
    }
  };

  const handleDrag = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true);
    } else if (e.type === 'dragleave') {
      setDragActive(false);
    }
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);
    
    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      handleDeviceFiles(e.dataTransfer.files);
    }
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.preventDefault();
    if (e.target.files && e.target.files[0]) {
      handleDeviceFiles(e.target.files);
    }
  };

  const handleRemoveUploadedFile = (idxToRemove: number) => {
    setUploadedFiles(prev => prev.filter((_, idx) => idx !== idxToRemove));
  };

  // UC03: Mock Attachment Selection & Size check
  const handleAddMockImage = (image: typeof MOCK_ATTACHMENT_IMAGES[0]) => {
    // BR-09: Size restriction limit (5MB). 
    if (image.size > 5 * 1024 * 1024) {
      setCustomFileError('Kích thước tập tin vượt quá giới hạn cho phép (5MB).');
      return;
    }

    if (selectedMockImages.some(img => img.name === image.name)) {
      // Remove if already added
      setSelectedMockImages(selectedMockImages.filter(img => img.name !== image.name));
    } else {
      setSelectedMockImages([...selectedMockImages, image]);
    }
  };

  // UC06: Keyword-Based Suggested Categorization
  const getSuggestedTopic = (text: string): FeedbackTopic => {
    const lowerText = text.toLowerCase();
    if (lowerText.includes('giao hàng') || lowerText.includes('vận chuyển') || lowerText.includes('ship') || lowerText.includes('shipper') || lowerText.includes('chậm')) {
      return FeedbackTopic.DELIVERY_ISSUE;
    }
    if (lowerText.includes('lỗi') || lowerText.includes('hỏng') || lowerText.includes('bug') || lowerText.includes('app sập') || lowerText.includes('đơ') || lowerText.includes('không bấm được')) {
      return FeedbackTopic.PRODUCT_ISSUE;
    }
    if (lowerText.includes('thái độ') || lowerText.includes('nhân viên') || lowerText.includes('tư vấn') || lowerText.includes('chăm sóc') || lowerText.includes('gọi điện')) {
      return FeedbackTopic.SERVICE_QUALITY;
    }
    if (lowerText.includes('ý kiến') || lowerText.includes('đề xuất') || lowerText.includes('tính năng') || lowerText.includes('nên có') || lowerText.includes('thêm')) {
      return FeedbackTopic.SUGGESTION;
    }
    if (lowerText.includes('đắt') || lowerText.includes('phí') || lowerText.includes('tiền') || lowerText.includes('kháng nghị') || lowerText.includes('gian lận')) {
      return FeedbackTopic.COMPLAINT;
    }
    return FeedbackTopic.OTHER;
  };

  // UC07 & UC08: Respond to Feedback & Update Status
  const handleAdminReply = (e: React.FormEvent) => {
    e.preventDefault();
    setAdminReplyError('');

    if (!currentUser || !selectedAdminFeedback) return;

    // BR-14: Response Validation (not blank)
    if (!adminReplyText.trim()) {
      setAdminReplyError('Nội dung phản hồi không được để trống.');
      return;
    }

    const newReply: Reply = {
      id: Date.now(),
      feedbackId: selectedAdminFeedback.id,
      adminId: currentUser.id,
      adminName: currentUser.name,
      content: adminReplyText,
      createdAt: new Date().toISOString()
    };

    const updatedFeedbacks = feedbacks.map(fb => {
      if (fb.id === selectedAdminFeedback.id) {
        return {
          ...fb,
          status: FeedbackStatus.RESOLVED, // BR-16: Automatic Status Update on successful reply
          updatedAt: new Date().toISOString(),
          replies: [...fb.replies, newReply]
        };
      }
      return fb;
    });

    saveState(users, updatedFeedbacks);
    
    // Update the drawer/detail view state as well
    const updatedFeedback = updatedFeedbacks.find(fb => fb.id === selectedAdminFeedback.id) || null;
    setSelectedAdminFeedback(updatedFeedback);
    setAdminReplyText('');
  };

  // UC08: Direct Update Feedback Status
  const handleDirectStatusUpdate = (feedbackId: number, status: FeedbackStatus) => {
    const updatedFeedbacks = feedbacks.map(fb => {
      if (fb.id === feedbackId) {
        return {
          ...fb,
          status,
          updatedAt: new Date().toISOString()
        };
      }
      return fb;
    });
    saveState(users, updatedFeedbacks);
    if (selectedAdminFeedback && selectedAdminFeedback.id === feedbackId) {
      setSelectedAdminFeedback({
        ...selectedAdminFeedback,
        status,
        updatedAt: new Date().toISOString()
      });
    }
  };

  // UC06: Direct Categorize Feedback Change
  const handleDirectCategorize = (feedbackId: number, topic: FeedbackTopic) => {
    const updatedFeedbacks = feedbacks.map(fb => {
      if (fb.id === feedbackId) {
        return {
          ...fb,
          topic,
          updatedAt: new Date().toISOString()
        };
      }
      return fb;
    });
    saveState(users, updatedFeedbacks);
    if (selectedAdminFeedback && selectedAdminFeedback.id === feedbackId) {
      setSelectedAdminFeedback({
        ...selectedAdminFeedback,
        topic,
        updatedAt: new Date().toISOString()
      });
    }
  };

  // UC09: Generate & Export Analytics Report (Mock triggers)
  const triggerExport = (type: string) => {
    setExportingReport(true);
    setExportSuccessMessage('');
    
    setTimeout(() => {
      setExportingReport(false);
      setExportSuccessMessage(`Xuất báo cáo thống kê ${type} thành công! Tải xuống hoàn tất.`);
    }, 2000);
  };

  // UC10: Manager - Create Admin/Staff Account
  const handleCreateUser = (e: React.FormEvent) => {
    e.preventDefault();
    setUserManageError('');

    if (!newUserName || !newUserEmail || !newUserPassword) {
      setUserManageError('Tên, Email và Mật khẩu không được để trống.');
      return;
    }

    // BR-04: Unique email
    if (users.some(u => u.email.toLowerCase() === newUserEmail.toLowerCase())) {
      setUserManageError('Địa chỉ email đã tồn tại trên hệ thống.');
      return;
    }

    const newUser: User = {
      id: Date.now(),
      email: newUserEmail,
      name: newUserName,
      role: newUserRole,
      isActive: true,
      phone: newUserPhone || undefined,
      department: newUserRole === RoleType.MANAGER ? newUserDept : undefined,
      specialization: newUserRole === RoleType.ADMIN ? newUserSpec : undefined
    };

    const updatedUsers = [...users, newUser];
    saveState(updatedUsers, feedbacks);
    setShowAddUserModal(false);

    // Reset fields
    setNewUserName('');
    setNewUserEmail('');
    setNewUserPassword('');
    setNewUserPhone('');
  };

  const toggleUserActiveStatus = (userId: number) => {
    const updatedUsers = users.map(u => {
      if (u.id === userId) {
        return { ...u, isActive: !u.isActive };
      }
      return u;
    });
    saveState(updatedUsers, feedbacks);
  };

  // --- DERIVED METRICS FOR MANAGER ANALYTICS ---
  const totalCount = feedbacks.length;
  const pendingCount = feedbacks.filter(f => f.status === FeedbackStatus.PENDING).length;
  const resolvedCount = feedbacks.filter(f => f.status === FeedbackStatus.RESOLVED).length;
  const averageRating = totalCount > 0 
    ? parseFloat((feedbacks.reduce((acc, f) => acc + f.rating, 0) / totalCount).toFixed(1))
    : 0;

  // Rating Distribution Calculations
  const ratingDistribution = [1, 2, 3, 4, 5].reduce((acc, star) => {
    acc[star] = feedbacks.filter(f => f.rating === star).length;
    return acc;
  }, {} as Record<number, number>);

  // Topic Distribution Calculations
  const topicDistribution = Object.values(FeedbackTopic).reduce((acc, topic) => {
    acc[topic] = feedbacks.filter(f => f.topic === topic).length;
    return acc;
  }, {} as Record<string, number>);


  // Filtered lists
  const filteredCustomerFeedbacks = feedbacks.filter(fb => {
    if (fb.customerId !== currentUser?.id) return false;
    if (customerStatusFilter !== 'ALL' && fb.status !== customerStatusFilter) return false;
    return true;
  });

  const filteredAdminFeedbacks = feedbacks.filter(fb => {
    // Search filter
    const matchesSearch = 
      fb.customerName.toLowerCase().includes(adminSearch.toLowerCase()) ||
      fb.customerEmail.toLowerCase().includes(adminSearch.toLowerCase()) ||
      fb.content.toLowerCase().includes(adminSearch.toLowerCase());
    
    // Topic filter
    const matchesTopic = adminTopicFilter === 'ALL' || fb.topic === adminTopicFilter;
    
    // Status filter
    const matchesStatus = adminStatusFilter === 'ALL' || fb.status === adminStatusFilter;

    return matchesSearch && matchesTopic && matchesStatus;
  });

  const getTopicVietnamese = (topic: FeedbackTopic) => {
    switch (topic) {
      case FeedbackTopic.PRODUCT_ISSUE: return 'Lỗi Sản Phẩm';
      case FeedbackTopic.SERVICE_QUALITY: return 'Chất Lượng Dịch Vụ';
      case FeedbackTopic.DELIVERY_ISSUE: return 'Giao Hàng & Vận Chuyển';
      case FeedbackTopic.SUGGESTION: return 'Góp Ý & Đề Xuất';
      case FeedbackTopic.COMPLAINT: return 'Khiếu Nại & Thắc Mắc';
      case FeedbackTopic.OTHER: return 'Khác';
    }
  };

  // --- RENDER COMPONENT ---
  return (
    <div id="app_root" class="min-h-screen bg-[#F8FAFC] flex flex-col font-sans text-slate-800 antialiased selection:bg-indigo-100 selection:text-indigo-950">
      
      {/* 1. Header Navigation */}
      <header id="app_header" class="bg-white/90 backdrop-blur-md border-b border-slate-200/60 sticky top-0 z-30 shadow-[0_2px_15px_-10px_rgba(0,0,0,0.05)]">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
          <div class="flex items-center space-x-3">
            <div class="p-2.5 bg-gradient-to-tr from-indigo-600 to-indigo-700 rounded-xl text-white shadow-sm">
              <MessageSquare class="w-5 h-5" />
            </div>
            <div>
              <span class="text-lg font-extrabold tracking-tight bg-gradient-to-r from-slate-900 to-slate-800 bg-clip-text text-transparent block leading-tight">FeedbackSystem</span>
              <span class="text-[10px] font-mono text-slate-400 font-bold tracking-wider block">SE104 SE_UIT</span>
            </div>
          </div>

          {currentUser ? (
            <div class="flex items-center space-x-4">
              <div class="text-right">
                <span class="block text-sm font-bold text-slate-900">{currentUser.name}</span>
                <span class="inline-flex items-center space-x-1.5 text-xs font-semibold text-slate-500">
                  {currentUser.role === RoleType.CUSTOMER && (
                    <span class="px-2.5 py-0.5 rounded-full bg-blue-50 text-blue-700 text-[10px] font-bold border border-blue-100">Khách Hàng</span>
                  )}
                  {currentUser.role === RoleType.ADMIN && (
                    <span class="px-2.5 py-0.5 rounded-full bg-indigo-50 text-indigo-700 text-[10px] font-bold border border-indigo-100">Quản Trị Viên</span>
                  )}
                  {currentUser.role === RoleType.MANAGER && (
                    <span class="px-2.5 py-0.5 rounded-full bg-emerald-50 text-emerald-700 text-[10px] font-bold border border-emerald-100">Quản Lý</span>
                  )}
                  <span class="w-1.5 h-1.5 rounded-full bg-green-500 animate-pulse inline-block"></span>
                </span>
              </div>
              <button 
                id="logout_btn"
                onClick={handleLogout}
                class="p-2.5 text-slate-400 hover:text-red-600 hover:bg-red-50 rounded-xl transition-all border border-transparent hover:border-red-100"
                title="Đăng xuất"
              >
                <LogOut class="w-4 h-4" />
              </button>
            </div>
          ) : (
            <div class="flex items-center space-x-1">
              <span class="px-3 py-1 bg-indigo-50 text-indigo-600 rounded-full text-xs font-semibold border border-indigo-100">Cổng dịch vụ trực tuyến</span>
            </div>
          )}
        </div>
      </header>

      {/* 2. Main Dashboard Layout */}
      <main class="flex-grow max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8 md:py-10">
        
        {/* --- AUTH GATEWAY --- */}
        {!currentUser && (
          <div id="auth_view" class="max-w-md mx-auto mt-12 bg-white p-8 rounded-[28px] border border-slate-200/80 shadow-[0_15px_40px_-15px_rgba(0,0,0,0.06)]">
            <div class="text-center mb-8">
              <div class="inline-flex p-3 bg-indigo-50 rounded-2xl text-indigo-600 mb-3 border border-indigo-100/50">
                <MessageSquare class="w-6 h-6" />
              </div>
              <h2 class="text-2xl font-bold text-slate-900">
                {showRegister ? 'Đăng Ký Tài Khoản' : 'Đăng Nhập Hệ Thống'}
              </h2>
              <p class="text-sm text-slate-500 mt-1">
                {showRegister 
                  ? 'Tạo tài khoản khách hàng để phản hồi ý kiến' 
                  : 'Xác thực tài khoản của bạn để truy cập hệ thống'
                }
              </p>
            </div>

            {/* Error alerts */}
            {loginError && (
              <div class="mb-5 p-4 bg-red-50 text-red-700 text-sm rounded-xl border border-red-200 flex items-start space-x-2">
                <AlertCircle class="w-5 h-5 shrink-0 mt-0.5" />
                <span>{loginError}</span>
              </div>
            )}
            {regError && (
              <div class="mb-5 p-4 bg-red-50 text-red-700 text-sm rounded-xl border border-red-200 flex items-start space-x-2">
                <AlertCircle class="w-5 h-5 shrink-0 mt-0.5" />
                <span>{regError}</span>
              </div>
            )}

            {!showRegister ? (
              /* LOGIN FORM */
              <form onSubmit={handleLogin} class="space-y-5">
                <div>
                  <label class="block text-sm font-semibold text-slate-700 mb-1.5">Địa chỉ Email</label>
                  <input 
                    type="email" 
                    placeholder="example@domain.com"
                    value={loginEmail}
                    onChange={e => setLoginEmail(e.target.value)}
                    class="w-full px-3.5 py-2.5 border border-slate-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all text-sm"
                  />
                </div>
                <div>
                  <label class="block text-sm font-semibold text-slate-700 mb-1.5">Mật khẩu</label>
                  <input 
                    type="password" 
                    placeholder="••••••••"
                    value={loginPassword}
                    onChange={e => setLoginPassword(e.target.value)}
                    class="w-full px-3.5 py-2.5 border border-slate-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all text-sm"
                  />
                </div>
                <button 
                  type="submit"
                  class="w-full py-3 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-xl shadow-md transition-all duration-150 text-sm"
                >
                  Đăng Nhập
                </button>
                <div class="text-center pt-2">
                  <span class="text-xs text-slate-500">Chưa có tài khoản? </span>
                  <button 
                    type="button" 
                    onClick={() => { setShowRegister(true); setLoginError(''); }}
                    class="text-xs text-blue-600 hover:underline font-semibold"
                  >
                    Đăng ký ngay
                  </button>
                </div>
              </form>
            ) : (
              /* REGISTER FORM */
              <form onSubmit={handleRegister} class="space-y-4">
                <div>
                  <label class="block text-sm font-semibold text-slate-700 mb-1">Họ và Tên</label>
                  <input 
                    type="text" 
                    placeholder="Nguyễn Văn A"
                    value={regName}
                    onChange={e => setRegName(e.target.value)}
                    class="w-full px-3.5 py-2.5 border border-slate-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all text-sm"
                  />
                </div>
                <div>
                  <label class="block text-sm font-semibold text-slate-700 mb-1">Vai Trò Tài Khoản</label>
                  <select
                    value={regRole}
                    onChange={e => setRegRole(e.target.value as RoleType)}
                    class="w-full px-3.5 py-2.5 border border-slate-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all text-sm bg-slate-50 font-semibold text-slate-700"
                  >
                    <option value={RoleType.CUSTOMER}>Khách Hàng (Customer)</option>
                    <option value={RoleType.ADMIN}>Nhân Viên Vận Hành (Admin)</option>
                    <option value={RoleType.MANAGER}>Quản Lý (Manager)</option>
                  </select>
                </div>

                {regRole === RoleType.ADMIN && (
                  <div class="space-y-1 animate-fade-in">
                    <label class="block text-sm font-semibold text-slate-700 mb-1">Lĩnh vực hỗ trợ / Chuyên môn</label>
                    <input 
                      type="text" 
                      placeholder="Ví dụ: Kỹ Thuật, Thanh Toán, Vận Đơn..."
                      value={regSpec}
                      onChange={e => setRegSpec(e.target.value)}
                      class="w-full px-3.5 py-2.5 border border-slate-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all text-sm"
                    />
                  </div>
                )}

                {regRole === RoleType.MANAGER && (
                  <div class="space-y-1 animate-fade-in">
                    <label class="block text-sm font-semibold text-slate-700 mb-1">Tên Phòng Ban Quản Lý</label>
                    <input 
                      type="text" 
                      placeholder="Ví dụ: Phòng Dịch Vụ Khách Hàng, Ban Điều Hành..."
                      value={regDept}
                      onChange={e => setRegDept(e.target.value)}
                      class="w-full px-3.5 py-2.5 border border-slate-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all text-sm"
                    />
                  </div>
                )}

                <div>
                  <label class="block text-sm font-semibold text-slate-700 mb-1">Địa chỉ Email</label>
                  <input 
                    type="email" 
                    placeholder="nguyenvana@gmail.com"
                    value={regEmail}
                    onChange={e => setRegEmail(e.target.value)}
                    class="w-full px-3.5 py-2.5 border border-slate-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all text-sm"
                  />
                </div>
                <div>
                  <label class="block text-sm font-semibold text-slate-700 mb-1">Số điện thoại (tùy chọn)</label>
                  <input 
                    type="text" 
                    placeholder="0912345678"
                    value={regPhone}
                    onChange={e => setRegPhone(e.target.value)}
                    class="w-full px-3.5 py-2.5 border border-slate-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all text-sm"
                  />
                </div>
                <div>
                  <label class="block text-sm font-semibold text-slate-700 mb-1">Mật khẩu</label>
                  <input 
                    type="password" 
                    placeholder="••••••••"
                    value={regPassword}
                    onChange={e => setRegPassword(e.target.value)}
                    class="w-full px-3.5 py-2.5 border border-slate-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition-all text-sm"
                  />
                </div>
                <button 
                  type="submit"
                  class="w-full py-3 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-xl shadow-md transition-all duration-150 text-sm"
                >
                  Đăng Ký Tài Khoản
                </button>
                <div class="text-center pt-2">
                  <span class="text-xs text-slate-500">Đã có tài khoản? </span>
                  <button 
                    type="button" 
                    onClick={() => { setShowRegister(false); setRegError(''); }}
                    class="text-xs text-blue-600 hover:underline font-semibold"
                  >
                    Quay lại đăng nhập
                  </button>
                </div>
              </form>
            )}


          </div>
        )}

        {/* --- 2A. CUSTOMER CLIENT INTERFACE --- */}
        {currentUser && currentUser.role === RoleType.CUSTOMER && (
          <div id="customer_portal" class="grid grid-cols-1 lg:grid-cols-12 gap-6 md:gap-8">
            
            {/* Left Side: Submit Feedback Form */}
            <div class="lg:col-span-5 bg-white p-6 sm:p-8 rounded-[28px] border border-slate-200/80 shadow-[0_8px_30px_rgb(0,0,0,0.02)] h-fit space-y-6">
              <div>
                <h2 class="text-xl font-extrabold text-slate-900 mb-1.5 flex items-center space-x-2">
                  <span>Gửi Ý Kiến Phản Hồi</span>
                  <Sparkles class="w-5 h-5 text-amber-500 animate-pulse" />
                </h2>
                <p class="text-xs text-slate-400 font-medium">Chúng tôi luôn lắng nghe để cải thiện chất lượng sản phẩm & dịch vụ tốt nhất.</p>
              </div>
              
              {submitSuccess && (
                <div class="p-4 bg-green-50/70 text-green-800 text-sm rounded-2xl border border-green-100 flex items-start space-x-2 animate-bounce">
                  <CheckCircle class="w-5 h-5 text-green-600 shrink-0 mt-0.5" />
                  <div>
                    <span class="font-bold block">Gửi phản hồi thành công!</span>
                    <span class="text-xs">Ý kiến của bạn đã được ghi nhận ở trạng thái <b class="text-green-900">Pending</b> và sẵn sàng để Admin xử lý.</span>
                  </div>
                </div>
              )}

              {customFileError && (
                <div class="p-4 bg-red-50/70 text-red-700 text-sm rounded-2xl border border-red-100 flex items-start space-x-2">
                  <AlertCircle class="w-5 h-5 text-red-600 shrink-0 mt-0.5" />
                  <span class="text-xs">{customFileError}</span>
                </div>
              )}

              <form onSubmit={handleFeedbackSubmit} class="space-y-5">
                
                {/* 1. Rating input */}
                <div class="space-y-1.5">
                  <label class="block text-xs font-bold uppercase tracking-wider text-slate-400 font-mono">Mức Độ Hài Lòng</label>
                  <div class="flex items-center space-x-1 bg-slate-50 p-2 rounded-xl border border-slate-150">
                    {[1, 2, 3, 4, 5].map((starValue) => (
                      <button
                        key={starValue}
                        type="button"
                        onClick={() => setNewFeedbackRating(starValue)}
                        class="p-1 focus:outline-none transition-transform active:scale-125"
                      >
                        <Star 
                          class={`w-7 h-7 transition-colors duration-150 ${
                            starValue <= newFeedbackRating 
                              ? 'text-yellow-400 fill-yellow-400' 
                              : 'text-slate-200'
                          }`} 
                        />
                      </button>
                    ))}
                    <span class="text-[11px] font-bold text-slate-600 ml-2">
                      {newFeedbackRating === 5 && 'Rất Hài Lòng (5★)'}
                      {newFeedbackRating === 4 && 'Hài Lòng (4★)'}
                      {newFeedbackRating === 3 && 'Bình Thường (3★)'}
                      {newFeedbackRating === 2 && 'Không Hài Lòng (2★)'}
                      {newFeedbackRating === 1 && 'Rất Tệ (1★)'}
                    </span>
                  </div>
                </div>

                {/* 2. Topic category */}
                <div class="space-y-1.5">
                  <label class="block text-xs font-bold uppercase tracking-wider text-slate-400 font-mono">Chủ đề phản hồi</label>
                  <select 
                    value={newFeedbackTopic} 
                    onChange={e => setNewFeedbackTopic(e.target.value as FeedbackTopic)}
                    class="w-full px-3.5 py-2.5 border border-slate-200 rounded-xl focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 outline-none transition-all text-xs bg-slate-50 font-medium text-slate-700"
                  >
                    <option value={FeedbackTopic.PRODUCT_ISSUE}>Lỗi Sản Phẩm & Ứng Dụng</option>
                    <option value={FeedbackTopic.SERVICE_QUALITY}>Chất Lượng Phục Vụ</option>
                    <option value={FeedbackTopic.DELIVERY_ISSUE}>Giao Hàng & Vận Chuyển</option>
                    <option value={FeedbackTopic.SUGGESTION}>Góp Ý & Đề Xuất Phát Triển</option>
                    <option value={FeedbackTopic.COMPLAINT}>Khiếu Nại & Thắc Mắc</option>
                    <option value={FeedbackTopic.OTHER}>Chủ Đề Khác</option>
                  </select>
                </div>

                {/* 3. Text content */}
                <div class="space-y-1.5">
                  <div class="flex justify-between items-center">
                    <label class="block text-xs font-bold uppercase tracking-wider text-slate-400 font-mono">Nội dung chi tiết</label>
                    <span class="text-[9px] text-indigo-600 font-mono font-bold uppercase tracking-wider">Bắt buộc</span>
                  </div>
                  <textarea 
                    rows={4}
                    placeholder="Hãy chia sẻ trải nghiệm thực tế của bạn tại đây để ban quản trị hỗ trợ bạn nhanh nhất có thể..."
                    value={newFeedbackContent}
                    onChange={e => setNewFeedbackContent(e.target.value)}
                    class="w-full px-3.5 py-2.5 border border-slate-200 rounded-xl focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 outline-none transition-all text-xs placeholder-slate-400 bg-slate-50 leading-relaxed"
                  ></textarea>
                </div>

                {/* 4. Attachment triggers */}
                <div class="space-y-3">
                  <div class="flex justify-between items-center">
                    <label class="block text-xs font-bold uppercase tracking-wider text-slate-400 font-mono">Đính Kèm Minh Họa</label>
                    <span class="text-[9px] text-slate-400 font-mono font-bold">Tối đa 5MB/tệp (Ảnh JPG/PNG)</span>
                  </div>

                  {/* Real File Upload Drag & Drop Area */}
                  <div 
                    onDragEnter={handleDrag}
                    onDragOver={handleDrag}
                    onDragLeave={handleDrag}
                    onDrop={handleDrop}
                    onClick={() => fileInputRef.current?.click()}
                    class={`border-2 border-dashed rounded-2xl p-4 text-center cursor-pointer transition-all duration-200 ${
                      dragActive 
                        ? 'border-indigo-500 bg-indigo-50/50 scale-[1.01]' 
                        : 'border-slate-200 bg-slate-50/50 hover:bg-slate-100 hover:border-slate-350'
                    }`}
                  >
                    <input 
                      ref={fileInputRef}
                      type="file"
                      multiple
                      accept="image/*"
                      onChange={handleFileChange}
                      class="hidden"
                    />
                    <div class="flex flex-col items-center justify-center space-y-1.5">
                      <div class="p-2 bg-indigo-50 text-indigo-600 rounded-xl">
                        <Upload class="w-4 h-4" />
                      </div>
                      <div class="text-xs">
                        <span class="font-bold text-indigo-600 hover:text-indigo-700">Tải tệp từ thiết bị của bạn</span>
                        <span class="text-slate-500 font-medium"> hoặc kéo thả vào đây</span>
                      </div>
                      <p class="text-[9px] text-slate-400 font-medium">Hỗ trợ tải lên nhiều hình ảnh thực tế</p>
                    </div>
                  </div>

                  {/* List of uploaded files from device */}
                  {uploadedFiles.length > 0 && (
                    <div class="space-y-1.5 max-h-32 overflow-y-auto pr-1">
                      <span class="text-[10px] font-bold text-slate-500 block">Tệp tin đã chọn ({uploadedFiles.length}):</span>
                      {uploadedFiles.map((file, idx) => (
                        <div key={idx} class="flex items-center justify-between p-2 bg-slate-50 border border-slate-150 rounded-xl text-[10px] animate-fade-in">
                          <div class="flex items-center space-x-2 min-w-0">
                            <img src={file.url} alt="upload-preview" class="w-6 h-6 object-cover rounded-md border border-slate-200" />
                            <span class="font-semibold text-slate-700 truncate max-w-[150px]">{file.name}</span>
                            <span class="text-slate-400 font-mono">({(file.size / 1024).toFixed(0)} KB)</span>
                          </div>
                          <button 
                            type="button" 
                            onClick={(e) => { e.stopPropagation(); handleRemoveUploadedFile(idx); }}
                            class="p-1 text-slate-400 hover:text-red-500 transition-colors"
                          >
                            <Trash2 class="w-3.5 h-3.5" />
                          </button>
                        </div>
                      ))}
                    </div>
                  )}
                  
                  {/* Select Mock Image buttons for quick testing */}
                  <div class="pt-2 border-t border-slate-100">
                    <span class="text-[10px] font-bold text-slate-400 block mb-1.5 uppercase font-mono tracking-wider">Hoặc chọn nhanh ảnh mẫu:</span>
                    <div class="grid grid-cols-3 gap-2">
                      {MOCK_ATTACHMENT_IMAGES.map((img) => {
                        const isSelected = selectedMockImages.some(i => i.name === img.name);
                        return (
                          <button
                            key={img.name}
                            type="button"
                            onClick={() => handleAddMockImage(img)}
                            class={`p-2 border rounded-xl flex flex-col items-center justify-between text-center transition-all ${
                              isSelected 
                                ? 'border-indigo-500 bg-indigo-50/40 ring-2 ring-indigo-100/50' 
                                : 'border-slate-150 bg-slate-50/50 hover:bg-white hover:border-slate-300'
                            }`}
                          >
                            <img src={img.url} alt="mock" class="w-8 h-8 object-cover rounded-md mb-1 border border-slate-100" referrerPolicy="no-referrer" />
                            <span class="text-[9px] font-bold text-slate-700 truncate w-full">{img.name}</span>
                            <span class="text-[8px] font-mono text-slate-400 font-medium">{(img.size / 1000).toFixed(0)} KB</span>
                          </button>
                        );
                      })}
                    </div>
                  </div>
                </div>

                <button 
                  type="submit"
                  class="w-full py-3 bg-gradient-to-r from-indigo-600 to-indigo-700 hover:from-indigo-700 hover:to-indigo-800 text-white font-bold rounded-xl shadow-[0_4px_15px_-3px_rgba(79,70,229,0.2)] hover:shadow-[0_4px_20px_-3px_rgba(79,70,229,0.3)] transition-all duration-200 text-xs flex items-center justify-center space-x-2"
                >
                  <Send class="w-3.5 h-3.5" />
                  <span>Gửi Ý Kiến Ngay</span>
                </button>

              </form>
            </div>

            {/* Right Side: History Feedbacks */}
            <div class="lg:col-span-7 bg-white p-6 sm:p-8 rounded-[28px] border border-slate-200/80 shadow-[0_8px_30px_rgb(0,0,0,0.02)] space-y-6">
              <div class="sm:flex sm:items-center sm:justify-between pb-4 border-b border-slate-100">
                <div>
                  <h2 class="text-xl font-extrabold text-slate-900 mb-1">Lịch Sử Phản Hồi</h2>
                  <p class="text-xs text-slate-400 font-medium">Xem tiến độ xử lý và phản hồi từ ban điều hành</p>
                </div>
                
                {/* History Status Filters */}
                <div class="mt-4 sm:mt-0 flex items-center space-x-1 bg-slate-50 p-1.5 rounded-xl border border-slate-150">
                  <span class="text-[10px] text-slate-400 font-bold uppercase tracking-wider font-mono px-1">Lọc:</span>
                  <button 
                    onClick={() => setCustomerStatusFilter('ALL')}
                    class={`px-2.5 py-1 text-xs font-bold rounded-lg transition-all ${
                      customerStatusFilter === 'ALL' 
                        ? 'bg-indigo-600 text-white shadow-xs' 
                        : 'text-slate-600 hover:bg-slate-200/70'
                    }`}
                  >
                    Tất cả
                  </button>
                  <button 
                    onClick={() => setCustomerStatusFilter(FeedbackStatus.PENDING)}
                    class={`px-2.5 py-1 text-xs font-bold rounded-lg transition-all ${
                      customerStatusFilter === FeedbackStatus.PENDING 
                        ? 'bg-amber-500 text-white shadow-xs' 
                        : 'text-amber-700 hover:bg-amber-100/55'
                    }`}
                  >
                    Đang Chờ
                  </button>
                  <button 
                    onClick={() => setCustomerStatusFilter(FeedbackStatus.RESOLVED)}
                    class={`px-2.5 py-1 text-xs font-bold rounded-lg transition-all ${
                      customerStatusFilter === FeedbackStatus.RESOLVED 
                        ? 'bg-emerald-600 text-white shadow-xs' 
                        : 'text-emerald-700 hover:bg-emerald-100/55'
                    }`}
                  >
                    Đã Xong
                  </button>
                </div>
              </div>

              {/* Feedback History Cards List */}
              <div class="space-y-4">
                {filteredCustomerFeedbacks.length === 0 ? (
                  <div class="text-center py-16 border border-dashed border-slate-200 rounded-3xl bg-slate-50/50">
                    <Clock class="w-8 h-8 text-slate-300 mx-auto mb-2 animate-pulse" />
                    <p class="text-slate-400 text-xs font-medium">Chưa có phản hồi nào khớp với bộ lọc của bạn.</p>
                  </div>
                ) : (
                  filteredCustomerFeedbacks.map((fb) => (
                    <div key={fb.id} class="p-6 border border-slate-100 bg-slate-50/40 hover:bg-white rounded-[20px] border-slate-200/50 hover:border-slate-200 hover:shadow-[0_10px_25px_-5px_rgba(0,0,0,0.02)] transition-all duration-300">
                      
                      {/* Top status info */}
                      <div class="flex items-center justify-between mb-3">
                        <div class="flex items-center space-x-2.5">
                          <span class={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold ${
                            fb.status === FeedbackStatus.PENDING 
                              ? 'bg-yellow-50 text-yellow-800 border border-yellow-100' 
                              : 'bg-green-50 text-green-800 border border-green-100'
                          }`}>
                            {fb.status === FeedbackStatus.PENDING ? 'Đang Chờ Duyệt' : 'Đã Giải Quyết'}
                          </span>
                          <span class="text-xs text-slate-500 font-medium">
                            {getTopicVietnamese(fb.topic)}
                          </span>
                        </div>
                        
                        <div class="flex space-x-0.5">
                          {[1, 2, 3, 4, 5].map((s) => (
                            <Star 
                              key={s} 
                              class={`w-4 h-4 ${s <= fb.rating ? 'text-yellow-400 fill-yellow-400' : 'text-slate-200'}`} 
                            />
                          ))}
                        </div>
                      </div>

                      {/* Main feedback text */}
                      <p class="text-sm text-slate-800 leading-relaxed mb-4 font-normal">
                        {fb.content}
                      </p>

                      {/* Attached images previews */}
                      {fb.attachments.length > 0 && (
                        <div class="flex space-x-2 mb-4">
                          {fb.attachments.map((att) => (
                            <div key={att.id} class="relative group cursor-pointer border border-slate-200 rounded-lg overflow-hidden">
                              <img src={att.url} alt={att.fileName} class="w-14 h-14 object-cover hover:scale-105 transition-all" referrerPolicy="no-referrer" />
                            </div>
                          ))}
                        </div>
                      )}

                      {/* Audit dates info */}
                      <div class="text-[10px] font-mono text-slate-400 flex justify-between items-center">
                        <span>Mã số: #{fb.id}</span>
                        <span>Ngày gửi: {new Date(fb.createdAt).toLocaleString('vi-VN')}</span>
                      </div>

                      {/* Admin responses list */}
                      {fb.replies.length > 0 && (
                        <div class="mt-4 p-4 bg-slate-50 border border-slate-150 rounded-xl relative">
                          <div class="absolute -top-2 left-5 w-4 h-4 bg-slate-50 border-t border-l border-slate-150 rotate-45"></div>
                          <div class="space-y-3">
                            {fb.replies.map((rep) => (
                              <div key={rep.id} class="space-y-1">
                                <div class="flex justify-between items-center">
                                  <span class="text-xs font-bold text-slate-900 flex items-center space-x-1">
                                    <Shield class="w-3.5 h-3.5 text-indigo-600 mr-1" />
                                    <span>{rep.adminName}</span>
                                  </span>
                                  <span class="text-[9px] font-mono text-slate-400">
                                    {new Date(rep.createdAt).toLocaleString('vi-VN')}
                                  </span>
                                </div>
                                <p class="text-xs text-slate-700 leading-relaxed font-normal">
                                  {rep.content}
                                </p>
                              </div>
                            ))}
                          </div>
                        </div>
                      )}

                    </div>
                  ))
                )}
              </div>
            </div>

          </div>
        )}

        {/* --- 2B. ADMIN WORKSPACE DASHBOARD --- */}
        {currentUser && currentUser.role === RoleType.ADMIN && (
          <div id="admin_portal" class="grid grid-cols-1 lg:grid-cols-12 gap-6 md:gap-8">
            
            {/* Left side: Master Feedbacks list with advanced search/filters */}
            <div class="lg:col-span-7 bg-white p-6 sm:p-8 rounded-[28px] border border-slate-200/80 shadow-[0_8px_30px_rgb(0,0,0,0.02)] h-fit space-y-6">
              <div class="pb-4 border-b border-slate-100 sm:flex sm:items-center sm:justify-between">
                <div>
                  <h2 class="text-xl font-extrabold text-slate-900 mb-1">Quản Lý Phản Hồi</h2>
                  <p class="text-xs text-slate-400 font-medium">Tiếp nhận, phân loại & trả lời khách hàng</p>
                </div>
                <span class="text-[11px] font-bold font-mono px-3 py-1 bg-indigo-50 border border-indigo-150 text-indigo-700 rounded-xl mt-2 sm:mt-0 inline-block shadow-xs">
                  Tổng số: {filteredAdminFeedbacks.length}
                </span>
              </div>

              {/* Advanced Controls bar */}
              <div class="space-y-4">
                <div class="relative">
                  <Search class="absolute left-3.5 top-3.5 w-4 h-4 text-slate-400" />
                  <input 
                    type="text" 
                    placeholder="Tìm theo tên khách hàng, email, nội dung..."
                    value={adminSearch}
                    onChange={e => setAdminSearch(e.target.value)}
                    class="w-full pl-10 pr-4 py-3 border border-slate-200 rounded-xl focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 outline-none transition-all text-xs bg-slate-50/60 font-medium placeholder-slate-400"
                  />
                </div>

                <div class="grid grid-cols-2 gap-3">
                  {/* Topic Filter */}
                  <div class="space-y-1">
                    <label class="block text-[10px] font-bold uppercase tracking-wider text-slate-400 font-mono">Chủ Đề</label>
                    <select 
                      value={adminTopicFilter}
                      onChange={e => setAdminTopicFilter(e.target.value as any)}
                      class="w-full px-3.5 py-2.5 border border-slate-200 rounded-xl text-xs bg-slate-50 font-medium text-slate-700 outline-none focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 transition-all"
                    >
                      <option value="ALL">Tất cả chủ đề</option>
                      <option value={FeedbackTopic.PRODUCT_ISSUE}>Lỗi Sản Phẩm</option>
                      <option value={FeedbackTopic.SERVICE_QUALITY}>Chất Lượng Phục Vụ</option>
                      <option value={FeedbackTopic.DELIVERY_ISSUE}>Giao Hàng</option>
                      <option value={FeedbackTopic.SUGGESTION}>Góp Ý</option>
                      <option value={FeedbackTopic.COMPLAINT}>Khiếu Nại</option>
                      <option value={FeedbackTopic.OTHER}>Khác</option>
                    </select>
                  </div>

                  {/* Status Filter */}
                  <div class="space-y-1">
                    <label class="block text-[10px] font-bold uppercase tracking-wider text-slate-400 font-mono">Trạng Thái</label>
                    <select 
                      value={adminStatusFilter}
                      onChange={e => setAdminStatusFilter(e.target.value as any)}
                      class="w-full px-3.5 py-2.5 border border-slate-200 rounded-xl text-xs bg-slate-50 font-medium text-slate-700 outline-none focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 transition-all"
                    >
                      <option value="ALL">Tất cả trạng thái</option>
                      <option value={FeedbackStatus.PENDING}>Đang Chờ (Pending)</option>
                      <option value={FeedbackStatus.RESOLVED}>Đã Giải Quyết (Resolved)</option>
                    </select>
                  </div>
                </div>
              </div>

              {/* Master Feedbacks list */}
              <div class="space-y-3">
                {filteredAdminFeedbacks.length === 0 ? (
                  <div class="text-center py-16 border border-dashed border-slate-200 rounded-3xl bg-slate-50/50">
                    <Search class="w-8 h-8 text-slate-300 mx-auto mb-2 animate-pulse" />
                    <p class="text-slate-400 text-xs font-medium">Không tìm thấy phản hồi nào.</p>
                  </div>
                ) : (
                  filteredAdminFeedbacks.map((fb) => {
                    const isSelected = selectedAdminFeedback?.id === fb.id;
                    return (
                      <button
                        key={fb.id}
                        type="button"
                        onClick={() => {
                          setSelectedAdminFeedback(fb);
                          setAdminReplyText('');
                        }}
                        class={`w-full p-5 border text-left rounded-2xl transition-all flex justify-between items-start ${
                          isSelected 
                            ? 'border-indigo-500/70 bg-indigo-50/30 shadow-[0_4px_15px_-3px_rgba(79,70,229,0.08)] ring-1 ring-indigo-100/50' 
                            : 'border-slate-100 bg-slate-50/40 hover:border-slate-200 hover:bg-white hover:shadow-[0_4px_15px_-3px_rgba(0,0,0,0.01)]'
                        }`}
                      >
                        <div class="space-y-2 shrink-0 w-[80%]">
                          <div class="flex items-center space-x-2">
                            <span class="font-semibold text-slate-950 text-sm">{fb.customerName}</span>
                            <span class="text-slate-300">•</span>
                            <span class="text-xs text-slate-500">{fb.customerEmail}</span>
                          </div>
                          
                          <p class="text-xs text-slate-700 font-normal line-clamp-2 leading-relaxed">
                            {fb.content}
                          </p>

                          <div class="flex items-center space-x-2">
                            <span class={`inline-flex items-center px-2 py-0.5 rounded-full text-[10px] font-bold ${
                              fb.status === FeedbackStatus.PENDING 
                                ? 'bg-yellow-100 text-yellow-800' 
                                : 'bg-green-100 text-green-800'
                            }`}>
                              {fb.status === FeedbackStatus.PENDING ? 'ĐANG CHỜ' : 'ĐÃ XONG'}
                            </span>
                            <span class="text-[10px] font-mono text-slate-400">
                              {getTopicVietnamese(fb.topic)}
                            </span>
                          </div>
                        </div>

                        <div class="flex flex-col items-end justify-between h-full space-y-4">
                          <div class="flex space-x-0.5">
                            {[1, 2, 3, 4, 5].map((s) => (
                              <Star 
                                key={s} 
                                class={`w-3.5 h-3.5 ${s <= fb.rating ? 'text-yellow-400 fill-yellow-400' : 'text-slate-200'}`} 
                              />
                            ))}
                          </div>
                          <span class="text-[9px] font-mono text-slate-400">
                            {new Date(fb.createdAt).toLocaleDateString('vi-VN')}
                          </span>
                        </div>
                      </button>
                    );
                  })
                )}
              </div>
            </div>

            {/* Right side: Detailed Feedback panel & Respond panel */}
            <div class="lg:col-span-5 bg-white p-6 sm:p-8 rounded-[28px] border border-slate-200/80 shadow-[0_8px_30px_rgb(0,0,0,0.02)] h-fit">
              {selectedAdminFeedback ? (
                <div class="space-y-6">
                  
                  {/* Title and control headers */}
                  <div class="pb-4 border-b border-slate-100 flex items-center justify-between">
                    <div>
                      <span class="text-[9px] font-bold font-mono text-indigo-500 tracking-wider block uppercase">ID: #{selectedAdminFeedback.id}</span>
                      <h3 class="text-base font-extrabold text-slate-900 block leading-tight">Chi Tiết Phản Hồi</h3>
                    </div>
                    
                    {/* Direct actions */}
                    <div class="flex items-center space-x-1.5">
                      {selectedAdminFeedback.status === FeedbackStatus.PENDING ? (
                        <button 
                          onClick={() => handleDirectStatusUpdate(selectedAdminFeedback.id, FeedbackStatus.RESOLVED)}
                          class="px-3 py-1.5 bg-emerald-50 hover:bg-emerald-100 text-emerald-700 text-xs font-bold rounded-xl flex items-center space-x-1 border border-emerald-100/75 transition-all shadow-xs"
                        >
                          <Check class="w-3.5 h-3.5" />
                          <span>Duyệt Xong</span>
                        </button>
                      ) : (
                        <button 
                          onClick={() => handleDirectStatusUpdate(selectedAdminFeedback.id, FeedbackStatus.PENDING)}
                          class="px-3 py-1.5 bg-amber-50 hover:bg-amber-100 text-amber-700 text-xs font-bold rounded-xl flex items-center space-x-1 border border-amber-100/75 transition-all shadow-xs"
                        >
                          <Clock class="w-3.5 h-3.5" />
                          <span>Mở Lại</span>
                        </button>
                      )}
                    </div>
                  </div>

                  {/* Customer Info Card */}
                  <div class="p-4 bg-slate-50/60 border border-slate-150 rounded-2xl space-y-1">
                    <span class="text-[9px] font-bold font-mono text-slate-400 uppercase tracking-wider block">Người gửi ý kiến</span>
                    <span class="font-extrabold text-slate-900 text-sm block">{selectedAdminFeedback.customerName}</span>
                    <span class="text-xs text-slate-500 font-medium block">{selectedAdminFeedback.customerEmail}</span>
                  </div>

                  {/* Core Content */}
                  <div class="space-y-1.5">
                    <span class="text-[9px] font-bold font-mono text-slate-400 uppercase tracking-wider block">Nội dung đóng góp</span>
                    <p class="text-xs text-slate-700 leading-relaxed font-medium bg-slate-50/30 p-3.5 rounded-2xl border border-slate-100">
                      {selectedAdminFeedback.content}
                    </p>
                  </div>

                  {/* Attachment Previews */}
                  {selectedAdminFeedback.attachments.length > 0 && (
                    <div class="space-y-1.5">
                      <span class="text-[9px] font-bold font-mono text-slate-400 uppercase tracking-wider block">Hình ảnh minh họa</span>
                      <div class="flex flex-wrap gap-2">
                        {selectedAdminFeedback.attachments.map((att) => (
                          <a 
                            key={att.id} 
                            href={att.url} 
                            target="_blank" 
                            rel="noreferrer" 
                            class="border border-slate-200 rounded-xl overflow-hidden shrink-0 hover:border-indigo-500 hover:ring-2 hover:ring-indigo-100 transition-all"
                          >
                            <img src={att.url} alt={att.fileName} class="w-16 h-16 object-cover" referrerPolicy="no-referrer" />
                          </a>
                        ))}
                      </div>
                    </div>
                  )}

                  {/* UC06: Suggest Auto-Categorize Sparkles */}
                  {selectedAdminFeedback.status === FeedbackStatus.PENDING && (
                    <div class="p-4 bg-amber-50/40 border border-amber-200/50 rounded-2xl flex items-start space-x-2.5 shadow-xs">
                      <Sparkles class="w-4 h-4 text-amber-500 shrink-0 mt-0.5" />
                      <div class="text-xs">
                        <span class="font-bold text-slate-800">Gợi ý phân loại AI: </span>
                        <span class="font-extrabold text-amber-800">{getTopicVietnamese(getSuggestedTopic(selectedAdminFeedback.content))}</span>
                        <div class="mt-1 flex space-x-2">
                          <button 
                            onClick={() => handleDirectCategorize(selectedAdminFeedback.id, getSuggestedTopic(selectedAdminFeedback.content))}
                            class="text-[10px] font-bold text-indigo-600 hover:text-indigo-800 hover:underline"
                          >
                            Áp dụng phân loại này
                          </button>
                        </div>
                      </div>
                    </div>
                  )}

                  {/* Categorize Control */}
                  <div class="space-y-1.5">
                    <label class="block text-[9px] font-bold uppercase tracking-wider text-slate-400 font-mono">Thay Đổi Phân Loại</label>
                    <div class="flex flex-wrap gap-1.5">
                      {Object.values(FeedbackTopic).map((topic) => {
                        const isSelected = selectedAdminFeedback.topic === topic;
                        return (
                          <button
                            key={topic}
                            onClick={() => handleDirectCategorize(selectedAdminFeedback.id, topic)}
                            class={`px-2.5 py-1 text-[10px] font-bold rounded-lg transition-all ${
                              isSelected 
                                ? 'bg-indigo-600 text-white shadow-xs' 
                                : 'bg-slate-100 text-slate-600 hover:bg-slate-200/70'
                            }`}
                          >
                            {getTopicVietnamese(topic)}
                          </button>
                        );
                      })}
                    </div>
                  </div>

                  {/* Responses list / Admin Replies history */}
                  <div class="pt-4 border-t border-slate-100 space-y-3">
                    <span class="text-[9px] font-bold font-mono text-slate-400 uppercase tracking-wider block">Lịch sử phản hồi từ Admin</span>
                    {selectedAdminFeedback.replies.length === 0 ? (
                      <p class="text-xs text-slate-400 italic font-medium">Chưa có phản hồi nào được gửi.</p>
                    ) : (
                      <div class="space-y-3">
                        {selectedAdminFeedback.replies.map((rep) => (
                          <div key={rep.id} class="p-3.5 bg-slate-50/70 rounded-2xl border border-slate-150 text-xs space-y-1.5">
                            <div class="flex justify-between items-center">
                              <span class="font-bold text-slate-900">{rep.adminName}</span>
                              <span class="font-mono text-[9px] text-slate-400 font-medium">{new Date(rep.createdAt).toLocaleString('vi-VN')}</span>
                            </div>
                            <p class="text-slate-600 leading-relaxed font-medium">{rep.content}</p>
                          </div>
                        ))}
                      </div>
                    )}
                  </div>

                  {/* Reply Input Box (UC07) */}
                  <div class="pt-4 border-t border-slate-100 space-y-3">
                    {adminReplyError && (
                      <div class="p-3 bg-red-50 text-red-700 text-xs rounded-xl border border-red-200">
                        {adminReplyError}
                      </div>
                    )}
                    <form onSubmit={handleAdminReply} class="space-y-3">
                      <label class="block text-xs font-bold uppercase tracking-wider text-slate-400 font-mono">Trả lời khách hàng</label>
                      <textarea 
                        rows={3} 
                        placeholder="Nhập nội dung trả lời khách hàng chi tiết và lịch sự. Hệ thống sẽ tự động chuyển trạng thái feedback sang Resolved..."
                        value={adminReplyText}
                        onChange={e => setAdminReplyText(e.target.value)}
                        class="w-full px-3.5 py-2.5 border border-slate-200 rounded-xl focus:ring-4 focus:ring-indigo-500/10 focus:border-indigo-500 outline-none transition-all text-xs bg-slate-50 placeholder-slate-400 leading-relaxed"
                      ></textarea>
                      <button 
                        type="submit"
                        class="w-full py-2.5 bg-gradient-to-r from-indigo-600 to-indigo-700 hover:from-indigo-700 hover:to-indigo-800 text-white font-bold rounded-xl text-xs flex items-center justify-center space-x-1.5 shadow-[0_4px_12px_rgba(79,70,229,0.15)] transition-all"
                      >
                        <Send class="w-3.5 h-3.5" />
                        <span>Gửi phản hồi và giải quyết</span>
                      </button>
                    </form>
                  </div>

                </div>
              ) : (
                <div class="text-center py-24 bg-slate-50/40 rounded-2xl border border-dashed border-slate-200">
                  <MessageSquare class="w-10 h-10 text-slate-300 mx-auto mb-3 animate-pulse" />
                  <h3 class="text-xs font-bold text-slate-700 uppercase tracking-wider font-mono">Chưa chọn phản hồi</h3>
                  <p class="text-[11px] text-slate-400 max-w-xs mx-auto mt-1 px-4 leading-relaxed font-medium">Hãy bấm chọn bất kỳ phản hồi nào trong danh sách bên trái để xem thông tin chi tiết và phản hồi khách hàng.</p>
                </div>
              )}
            </div>

          </div>
        )}

        {/* --- 2C. MANAGER WORKSPACE DASHBOARD --- */}
        {currentUser && currentUser.role === RoleType.MANAGER && (
          <div id="manager_portal" class="space-y-6 md:space-y-8 animate-fade-in">
            
            {/* Top Stat row (UC09 statistics) */}
            <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
              <div class="bg-white p-6 rounded-[24px] border border-slate-200/80 shadow-[0_8px_30px_rgb(0,0,0,0.015)] relative overflow-hidden group hover:border-slate-300 transition-all duration-300">
                <div class="absolute right-4 top-4 p-2 bg-indigo-50 text-indigo-600 rounded-xl border border-indigo-100">
                  <MessageSquare class="w-4 h-4" />
                </div>
                <span class="text-xs font-bold text-slate-400 uppercase tracking-wider block font-mono">Tổng Feedback</span>
                <span class="text-3xl font-extrabold text-slate-900 mt-2 block leading-none">{totalCount}</span>
                <span class="text-[10px] text-slate-400 mt-2 block font-medium">Toàn bộ lịch sử hệ thống</span>
              </div>
              <div class="bg-white p-6 rounded-[24px] border border-slate-200/80 shadow-[0_8px_30px_rgb(0,0,0,0.015)] relative overflow-hidden group hover:border-slate-300 transition-all duration-300">
                <div class="absolute right-4 top-4 p-2 bg-amber-50 text-amber-600 rounded-xl border border-amber-100">
                  <Clock class="w-4 h-4" />
                </div>
                <span class="text-xs font-bold text-slate-400 uppercase tracking-wider block font-mono">Chưa giải quyết</span>
                <span class="text-3xl font-extrabold text-amber-600 mt-2 block leading-none">{pendingCount}</span>
                <span class="text-[10px] text-amber-500 font-semibold mt-2 block">{(totalCount > 0 ? (pendingCount / totalCount * 100).toFixed(0) : 0)}% cần xử lý ngay</span>
              </div>
              <div class="bg-white p-6 rounded-[24px] border border-slate-200/80 shadow-[0_8px_30px_rgb(0,0,0,0.015)] relative overflow-hidden group hover:border-slate-300 transition-all duration-300">
                <div class="absolute right-4 top-4 p-2 bg-emerald-50 text-emerald-600 rounded-xl border border-emerald-100">
                  <CheckCircle class="w-4 h-4" />
                </div>
                <span class="text-xs font-bold text-slate-400 uppercase tracking-wider block font-mono">Đã giải quyết</span>
                <span class="text-3xl font-extrabold text-emerald-600 mt-2 block leading-none">{resolvedCount}</span>
                <span class="text-[10px] text-emerald-500 font-semibold mt-2 block">{(totalCount > 0 ? (resolvedCount / totalCount * 100).toFixed(0) : 0)}% tỉ lệ hoàn tất</span>
              </div>
              <div class="bg-white p-6 rounded-[24px] border border-slate-200/80 shadow-[0_8px_30px_rgb(0,0,0,0.015)] relative overflow-hidden group hover:border-slate-300 transition-all duration-300">
                <div class="absolute right-4 top-4 p-2 bg-yellow-50 text-yellow-600 rounded-xl border border-yellow-100">
                  <Star class="w-4 h-4 fill-yellow-400 text-yellow-500" />
                </div>
                <span class="text-xs font-bold text-slate-400 uppercase tracking-wider block font-mono">Điểm trung bình</span>
                <div class="flex items-baseline space-x-1 mt-2">
                  <span class="text-3xl font-extrabold text-slate-900 leading-none">{averageRating}</span>
                  <span class="text-xs font-bold text-yellow-500">/ 5★</span>
                </div>
                <div class="flex space-x-0.5 mt-2">
                  {[1, 2, 3, 4, 5].map((s) => (
                    <Star 
                      key={s} 
                      class={`w-2.5 h-2.5 ${s <= Math.round(averageRating) ? 'text-yellow-400 fill-yellow-400' : 'text-slate-200'}`} 
                    />
                  ))}
                </div>
              </div>
            </div>

            {/* TAB PANELS: 1. Analytics & Reporting, 2. Staff User Management */}
            <div class="grid grid-cols-1 lg:grid-cols-12 gap-6 md:gap-8">
              
              {/* UC09: Analytics & Reporting Visualization column */}
              <div class="lg:col-span-8 bg-white p-6 sm:p-8 rounded-[28px] border border-slate-200/80 shadow-[0_8px_30px_rgb(0,0,0,0.02)] space-y-8">
                
                {/* Control Header */}
                <div class="sm:flex sm:items-center sm:justify-between pb-4 border-b border-slate-100">
                  <div>
                    <h3 class="text-lg font-extrabold text-slate-900 mb-0.5">Biểu Đồ Thống Kê & Báo Cáo</h3>
                    <p class="text-xs text-slate-400 font-medium">Thống kê xu hướng hài lòng và chủ đề khiếu nại</p>
                  </div>
                  
                  {/* Reporting filters (BR-20) */}
                  <div class="mt-4 sm:mt-0 flex items-center space-x-2">
                    <div class="flex items-center space-x-1.5 bg-slate-50 p-1.5 rounded-xl border border-slate-150">
                      <Calendar class="w-3.5 h-3.5 text-slate-400" />
                      <input 
                        type="date" 
                        value={managerStartDate}
                        onChange={e => setManagerStartDate(e.target.value)}
                        class="bg-transparent border-none text-[11px] font-bold text-slate-600 outline-none w-24 font-mono cursor-pointer"
                      />
                      <span class="text-slate-300 text-xs">-</span>
                      <input 
                        type="date" 
                        value={managerEndDate}
                        onChange={e => setManagerEndDate(e.target.value)}
                        class="bg-transparent border-none text-[11px] font-bold text-slate-600 outline-none w-24 font-mono cursor-pointer"
                      />
                    </div>
                  </div>
                </div>

                {/* EXPORT OPTIONS OVERLAY BAR */}
                <div class="p-4 bg-slate-50/70 border border-slate-150 rounded-2xl sm:flex sm:items-center sm:justify-between">
                  <div class="space-y-0.5">
                    <span class="text-xs font-bold text-slate-900 block">Xuất báo cáo chi tiết</span>
                    <span class="text-[10px] text-slate-400 font-medium block">Hỗ trợ các cuộc họp đánh giá chất lượng phòng ban</span>
                  </div>
                  
                  <div class="mt-3 sm:mt-0 flex space-x-2">
                    <button 
                      onClick={() => triggerExport('PDF')}
                      disabled={exportingReport}
                      class="px-3 py-2 bg-red-50 hover:bg-red-100 text-red-700 text-xs font-semibold rounded-lg flex items-center space-x-1 border border-red-100"
                    >
                      <Download class="w-3.5 h-3.5" />
                      <span>PDF Report</span>
                    </button>
                    <button 
                      onClick={() => triggerExport('Excel')}
                      disabled={exportingReport}
                      class="px-3 py-2 bg-green-50 hover:bg-green-100 text-green-700 text-xs font-semibold rounded-lg flex items-center space-x-1 border border-green-100"
                    >
                      <Download class="w-3.5 h-3.5" />
                      <span>Excel Report</span>
                    </button>
                    <button 
                      onClick={() => triggerExport('CSV')}
                      disabled={exportingReport}
                      class="px-3 py-2 bg-blue-50 hover:bg-blue-100 text-blue-700 text-xs font-semibold rounded-lg flex items-center space-x-1 border border-blue-100"
                    >
                      <Download class="w-3.5 h-3.5" />
                      <span>CSV Data</span>
                    </button>
                  </div>
                </div>

                {/* Progress notifications for export */}
                {exportingReport && (
                  <div class="p-4 bg-blue-50 text-blue-800 text-sm rounded-xl border border-blue-200 flex items-center space-x-3 animate-pulse">
                    <Clock class="w-5 h-5 text-blue-600 animate-spin" />
                    <span>Hệ thống đang xuất nén dữ liệu báo cáo chất lượng cao... Vui lòng đợi trong giây lát.</span>
                  </div>
                )}
                {exportSuccessMessage && (
                  <div class="p-4 bg-green-50 text-green-800 text-sm rounded-xl border border-green-200 flex items-center space-x-3">
                    <CheckCircle class="w-5 h-5 text-green-600 shrink-0" />
                    <span>{exportSuccessMessage}</span>
                  </div>
                )}

                {/* Visual Custom SVGs Charts representing Drizzle dataset directly */}
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
                  
                  {/* Rating Distribution Bar Chart */}
                  <div class="p-5 border border-slate-150 rounded-xl space-y-4">
                    <span class="text-xs font-bold text-slate-800 block uppercase tracking-wider font-mono text-slate-400">Phân Phối Điểm Số Đánh Giá</span>
                    <div class="space-y-3">
                      {[5, 4, 3, 2, 1].map((star) => {
                        const count = ratingDistribution[star] || 0;
                        const percentage = totalCount > 0 ? (count / totalCount) * 100 : 0;
                        return (
                          <div key={star} class="flex items-center space-x-2 text-xs">
                            <span class="w-8 font-bold font-mono">{star} Sao</span>
                            <div class="flex-grow bg-slate-100 h-3 rounded-full overflow-hidden">
                              <div 
                                class="bg-yellow-400 h-full rounded-full transition-all duration-1000" 
                                style={{ width: `${percentage}%` }}
                              ></div>
                            </div>
                            <span class="w-8 text-right font-semibold font-mono">{count} fb</span>
                          </div>
                        );
                      })}
                    </div>
                  </div>

                  {/* Topic Distribution Column list */}
                  <div class="p-5 border border-slate-150 rounded-xl space-y-4">
                    <span class="text-xs font-bold text-slate-800 block uppercase tracking-wider font-mono text-slate-400">Tần Suất Theo Chủ Đề Phản Hồi</span>
                    <div class="space-y-3">
                      {Object.values(FeedbackTopic).map((topic) => {
                        const count = topicDistribution[topic] || 0;
                        const percentage = totalCount > 0 ? (count / totalCount) * 100 : 0;
                        return (
                          <div key={topic} class="space-y-1 text-xs">
                            <div class="flex justify-between font-semibold">
                              <span>{getTopicVietnamese(topic)}</span>
                              <span class="font-mono text-slate-500">{count} phản hồi ({(percentage).toFixed(0)}%)</span>
                            </div>
                            <div class="bg-slate-100 h-2.5 rounded-full overflow-hidden">
                              <div 
                                class="bg-indigo-600 h-full rounded-full transition-all duration-1000" 
                                style={{ width: `${percentage}%` }}
                              ></div>
                            </div>
                          </div>
                        );
                      })}
                    </div>
                  </div>

                </div>

              </div>

              {/* UC10: User / Admin Staff Accounts Management Workspace */}
              <div class="lg:col-span-4 bg-white p-6 sm:p-8 rounded-[28px] border border-slate-200/80 shadow-[0_8px_30px_rgb(0,0,0,0.02)] space-y-6 h-fit">
                <div class="pb-4 border-b border-slate-100 flex items-center justify-between">
                  <div>
                    <h3 class="text-base font-extrabold text-slate-900">Nhân Viên Vận Hành</h3>
                    <p class="text-[11px] text-slate-400 font-medium">Phân quyền quản trị hệ thống</p>
                  </div>
                  
                  <button 
                    onClick={() => {
                      setUserManageError('');
                      setShowAddUserModal(true);
                    }}
                    class="p-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl shadow-md transition-all duration-200"
                    title="Thêm tài khoản nhân viên"
                  >
                    <UserPlus class="w-4 h-4" />
                  </button>
                </div>

                {/* List of personnel registered on the platform */}
                <div class="space-y-3">
                  {users.map((u) => {
                    if (u.role === RoleType.CUSTOMER) return null; // Only show admins/managers here
                    return (
                      <div key={u.id} class="p-4 bg-slate-50/50 border border-slate-150 rounded-2xl space-y-3 hover:bg-white hover:border-slate-200 hover:shadow-xs transition-all duration-200">
                        <div class="flex items-start justify-between">
                          <div class="space-y-1">
                            <span class="font-bold text-slate-900 text-sm block leading-tight">{u.name}</span>
                            <span class="text-xs font-mono text-slate-500 block">{u.email}</span>
                          </div>
                          
                          <button 
                            onClick={() => toggleUserActiveStatus(u.id)}
                            class={`px-2 py-1 text-[10px] font-bold rounded-lg ${
                              u.isActive 
                                ? 'bg-green-50 text-green-700 border border-green-200 hover:bg-green-100' 
                                : 'bg-red-50 text-red-700 border border-red-200 hover:bg-red-100'
                            }`}
                          >
                            {u.isActive ? 'ACTIVE' : 'LOCKED'}
                          </button>
                        </div>

                        <div class="flex items-center justify-between text-[10px] text-slate-500 pt-2 border-t border-slate-100 font-semibold">
                          <span class="flex items-center">
                            {u.role === RoleType.ADMIN ? (
                              <span class="px-2.5 py-0.5 rounded-full bg-indigo-50 text-indigo-700 font-bold border border-indigo-100">Quản Trị</span>
                            ) : (
                              <span class="px-2.5 py-0.5 rounded-full bg-emerald-50 text-emerald-700 font-bold border border-emerald-100">Ban Quản Lý</span>
                            )}
                          </span>
                          
                          <span class="text-slate-400 font-medium">
                            {u.role === RoleType.ADMIN ? `Support: ${u.specialization}` : `Phòng: ${u.department}`}
                          </span>
                        </div>
                      </div>
                    );
                  })}
                </div>

              </div>

            </div>

            {/* UC10: ADD USER MODAL PANEL */}
            {showAddUserModal && (
              <div class="fixed inset-0 bg-slate-900/60 backdrop-blur-xs z-50 flex items-center justify-center p-4">
                <div class="bg-white w-full max-w-md p-6 sm:p-8 rounded-2xl border border-slate-200 shadow-2xl space-y-6">
                  
                  <div class="pb-3 border-b border-slate-100 flex justify-between items-center">
                    <h3 class="text-lg font-bold text-slate-950">Tạo Tài Khoản Nhân Viên Mới</h3>
                    <button onClick={() => setShowAddUserModal(false)} class="text-slate-400 hover:text-slate-600 text-lg">×</button>
                  </div>

                  {userManageError && (
                    <div class="p-3 bg-red-50 text-red-700 text-xs rounded-xl border border-red-200">
                      {userManageError}
                    </div>
                  )}

                  <form onSubmit={handleCreateUser} class="space-y-4">
                    
                    <div>
                      <label class="block text-xs font-semibold text-slate-700 mb-1">Tên Nhân Viên</label>
                      <input 
                        type="text" 
                        placeholder="Trần Văn D"
                        value={newUserName}
                        onChange={e => setNewUserName(e.target.value)}
                        class="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm outline-none focus:border-blue-500"
                        required
                      />
                    </div>

                    <div>
                      <label class="block text-xs font-semibold text-slate-700 mb-1">Địa chỉ Email</label>
                      <input 
                        type="email" 
                        placeholder="staff@gmail.com"
                        value={newUserEmail}
                        onChange={e => setNewUserEmail(e.target.value)}
                        class="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm outline-none focus:border-blue-500"
                        required
                      />
                    </div>

                    <div>
                      <label class="block text-xs font-semibold text-slate-700 mb-1">Mật khẩu khởi tạo</label>
                      <input 
                        type="password" 
                        placeholder="••••••••"
                        value={newUserPassword}
                        onChange={e => setNewUserPassword(e.target.value)}
                        class="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm outline-none focus:border-blue-500"
                        required
                      />
                    </div>

                    <div>
                      <label class="block text-xs font-semibold text-slate-700 mb-1">Vai Trò Hệ Thống (RBAC)</label>
                      <select 
                        value={newUserRole}
                        onChange={e => setNewUserRole(e.target.value as RoleType)}
                        class="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm outline-none bg-white"
                      >
                        <option value={RoleType.ADMIN}>Quản Trị Viên (Admin)</option>
                        <option value={RoleType.MANAGER}>Quản Lý Cấp Cao (Manager)</option>
                      </select>
                    </div>

                    {newUserRole === RoleType.ADMIN ? (
                      <div>
                        <label class="block text-xs font-semibold text-slate-700 mb-1">Chuyên Môn Quản Trị</label>
                        <select 
                          value={newUserSpec}
                          onChange={e => setNewUserSpec(e.target.value)}
                          class="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm outline-none bg-white"
                        >
                          <option value="TECHNICAL_SUPPORT">Hỗ Trợ Kỹ Thuật (Tech)</option>
                          <option value="CUSTOMER_RELATIONS">Chăm Sóc Khách Hàng</option>
                          <option value="PRODUCT_QUALITY">Chất Lượng Sản Phẩm</option>
                        </select>
                      </div>
                    ) : (
                      <div>
                        <label class="block text-xs font-semibold text-slate-700 mb-1">Phòng Ban Quản Lý</label>
                        <select 
                          value={newUserDept}
                          onChange={e => setNewUserDept(e.target.value)}
                          class="w-full px-3 py-2 border border-slate-300 rounded-lg text-sm outline-none bg-white"
                        >
                          <option value="CUSTOMER_SUCCESS">Customer Success</option>
                          <option value="OPERATIONS">Operations (Vận Hành)</option>
                          <option value="PRODUCT_MANAGEMENT">Product Management</option>
                        </select>
                      </div>
                    )}

                    <div class="flex items-center justify-end space-x-2 pt-4 border-t border-slate-100">
                      <button 
                        type="button" 
                        onClick={() => setShowAddUserModal(false)}
                        class="px-4 py-2 border border-slate-200 text-xs font-semibold rounded-lg text-slate-700 hover:bg-slate-50"
                      >
                        Hủy
                      </button>
                      <button 
                        type="submit"
                        class="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-xs font-semibold rounded-lg text-white"
                      >
                        Tạo Tài Khoản
                      </button>
                    </div>

                  </form>

                </div>
              </div>
            )}

          </div>
        )}

      </main>

      {/* 3. Footer system Credits */}
      <footer id="app_footer" class="bg-white border-t border-slate-200 py-6 mt-12">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center sm:flex sm:justify-between sm:items-center">
          <span class="text-xs text-slate-400 font-medium">© 2026 Customer Feedback System | Trường Đại học Công nghệ Thông tin - ĐHQG TP.HCM.</span>
          <span class="text-xs font-mono text-slate-300 block mt-2 sm:mt-0">Built under SE104 requirements</span>
        </div>
      </footer>

    </div>
  );
}
