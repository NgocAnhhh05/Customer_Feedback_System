export enum RoleType {
  CUSTOMER = 'CUSTOMER',
  ADMIN = 'ADMIN',
  MANAGER = 'MANAGER'
}

export enum FeedbackStatus {
  PENDING = 'PENDING',
  RESOLVED = 'RESOLVED'
}

export enum FeedbackTopic {
  PRODUCT_ISSUE = 'PRODUCT_ISSUE',
  SERVICE_QUALITY = 'SERVICE_QUALITY',
  DELIVERY_ISSUE = 'DELIVERY_ISSUE',
  SUGGESTION = 'SUGGESTION',
  COMPLAINT = 'COMPLAINT',
  OTHER = 'OTHER'
}

export interface User {
  id: number;
  email: string;
  name: string;
  role: RoleType;
  isActive: boolean;
  phone?: string;
  department?: string; // For managers/admins
  specialization?: string; // For admins
}

export interface Attachment {
  id: number;
  fileName: string;
  fileType: string;
  fileSize: number;
  url: string;
}

export interface Reply {
  id: number;
  feedbackId: number;
  adminId: number;
  adminName: string;
  content: string;
  createdAt: string;
}

export interface Feedback {
  id: number;
  customerId: number;
  customerName: string;
  customerEmail: string;
  content: string;
  rating: number; // 1 to 5
  topic: FeedbackTopic;
  status: FeedbackStatus;
  createdAt: string;
  updatedAt: string;
  attachments: Attachment[];
  replies: Reply[];
}
