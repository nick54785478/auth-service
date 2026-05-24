# AuthService - 多服務 RBAC 權限中心

## 專案介紹
AuthService 是一個基於 RBAC (Role-Based Access Control) 模型開發的會員與權限管理中心。
不同於傳統單一系統的權限模組，本系統設計支援 多服務架構 (Multi-Service)，能夠作為企業內部的統一權限中心（Identity & Access Management），
為旗下多個微服務或前端應用提供具備邊界隔離的客製化群組、角色與功能設定。

## 核心業務模型：RBAC 架構 (Role-Based Access Control)

本專案採用業界標準的 RBAC 模型來設計權限系統，解決了傳統「權限直接綁定使用者」所帶來的高管理成本與維護災難。
為了應對企業內部複雜的組織架構與例外情境，本系統將角色賦予的方式拆分為兩條獨立但可疊加的軌道。

### 雙軌繼承授權路徑 群組角色 vs. 個人角色

**1. 群組角色 (Group Role)：基於組織架構的批量授權**
>* **路徑**：User -> Group -> Role -> Function
>* **情境**：針對企業中的「部門」或「專案團隊」設計。將多個基礎角色打包成一個 Group（例如：財務部 群組包含 報表管理員 與 帳務瀏覽者）。
>* **優勢**：當新員工入職或部門調動時，只需變更其所屬群組，即可瞬間完成大量權限的配置與回收，極大化降低日常維護成本。

**2. 個人角色 (Personal Role)：基於特殊任務的精準授權**
>* **路徑**：User -> Role -> Function
>* **情境**：針對特定人員的例外管理或跨部門任務。例如：某位工程師雖然屬於 一般研發部 群組，但因支援特殊專案，被直接賦予了 專案 DBA 的個人角色。
>* **優勢**：提供極高的操作彈性，允許在不破壞既有群組規則的前提下，為單一使用者進行權限微調與例外處理。

**權限疊加原則**：
> 系統最終在判定使用者權限時，會將其擁有的「個人角色」與所屬的「群組角色」進行聯集 (Union)，確保使用者能無縫取得所有應具備的系統功能 (Function)。

### 結合 Multi-Service (多服務) 的獨家優勢

本系統的每個 RBAC 節點（Group, Role, Function）都內建了 Service Scope（服務邊界）。
這意味著 User A 可以同時是「商城系統 (Service: Shop)」的 一般會員，以及「後台管理系統 (Service: Admin)」的 超級管理員。
兩套系統的權限節點在資料庫層級互不干擾，完美實現單一中心、多重子系統的企業級權限控管。

## 核心業務功能 (Features)

>* 雙軌 RBAC 模型：支援「群組角色」與「個人角色」疊加機制，輕鬆應對企業級複雜組織架構與例外授權。
>* 多服務隔離 (Service Isolation)：透過 Service Scope 設計，確保各子系統的權限設定絕對隔離，實現單一實體支援多租戶的彈性。
>* 動態個人化配置 (Customisation)：內建客製化聚合，支援各前端應用的 UI 個人化偏好設定（如動態表格欄位配置），並採用 JSON 序列化儲存提供極大彈性。
>* 安全與效能優化：內建 JWT Refresh Token 機制，並針對權限交集比對進行集合論優化，減少資料庫不必要的載入。

## 技術亮點與架構實踐 (Technical Highlights)

本專案拒絕傳統的「貧血模型 (Anemic Domain Model)」與義大利麵條式的 CRUD，深度實踐了 Domain-Driven Design (DDD) 與 Clean Architecture 原則，展現對高質量軟體工程的追求。

**1. 豐富的領域模型 (Rich Domain Model) 與 Value Object**

>* 將高度相關的屬性（如：service 與 code）封裝為 Value Object (Scope, Profile)，消滅基本型別偏執 (Primitive Obsession)。
>* 嚴格保護 Aggregate Root：移除無參建構子與萬能 Setter，所有狀態變更皆透過具有明確業務意圖的方法（如 updateProfile, changePassword）執行，確保實體生命週期的合法性。

**2. 堅實的架構邊界 (Clean Architecture)**

>* 防腐層隔離：Application Layer 的 Command 與技術細節（如 JSON 序列化工具、PasswordEncoder 加密演算法）絕不入侵 Domain Layer。領域實體保持 100% 純 Java 環境。
>* 資料傳輸保護：實踐嚴格的 DTO 轉換機制。透過客製化的 Assembler，將資料庫 Entity 轉換為前端所需的扁平化 DTO，避免 JPA Lazy Loading 異常與領域知識外洩。

**3. 優雅的資料庫操作 (Spring Data JPA)**

>* Upsert 模式優化：利用 Java Optional 的 .map().orElseGet() 語法，優雅實現無鎖的「有則更新、無則新增」邏輯。
>* 查詢效能下推：摒棄將資料全部拉回 Java 記憶體進行 filter 的做法，大量使用 Spring Data JPA Specification 將條件下推至資料庫層級（如 findByIdInAndScopeServiceAndActiveFlag）進行高效集合檢索。

## 核心領域設計 (Domain Aggregates)

| AggregateRoot | 職責 | VO
| --- | --- | ---|
| UserInfo | 會員聚合根，管理憑證、個人檔案及所屬群組/角色。 | UserProfile |
| RoleInfo	| 角色聚合根，定義特定服務下的角色，並綁定具體功能。 |	RoleScope, RoleProfile |
| GroupInfo |	群組聚合根，將多個角色打包，方便大量使用者的權限指派。 |	GroupScope, GroupProfile |
| FunctionInfo | 功能聚合根，系統中最小的權限控制單元 (如 API 路由、按鈕)。 | FunctionScope, FunctionProfile |
| Customisation | 客製化聚合根，管理使用者的跨服務/跨元件偏好設定。 | CustomisationScope |

## 前後端環境、框架及外部依賴

**後端環境**
>* Java
>* Spring Boot 3.3.5
>* JDK 17
>* Lombok & ModelMapper (簡化代碼與物件轉換)
>* Maven 3.9.12

**前端環境**
>* Angular 18.2.0
>* PrimeNG 17.18.12
>* Primeflex 3.3.1
>* node.js 22.11.0

**外部依賴**
>* MySQL
>* Docker

