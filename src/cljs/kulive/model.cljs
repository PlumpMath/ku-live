(ns kulive.model
  (:require [clojure.string :as str]
            [reagent.core :as reagent]
            [re-frame.core :as re-frame])
  (:require-macros [reagent.ratom  :refer [reaction]]))

(def dummy-data {"DISS352-00" {:kr {:professor "이지수", :schedule ["화(1) 국제관322" "\n목(1) 국제관322"], :r false, :number "DISS352-00", :name " 국제법특강I(영강)", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  3)", :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=3928&dept_cd=3931&cour_cd=DISS352&cour_cls=00&cour_nm=국제법특강I(영강)"}, :en {:professor nil, :schedule ["Tue(1) 138-322" "\nThu(1) 138-322"], :r false, :number "DISS352-00", :name "SPECIAL TOPICS IN INTERNATIONAL LAW I(English)", :w false, :l true, :classification "Major Elective", :credit-hours "3(  3)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=3928&dept_cd=3931&cour_cd=DISS352&cour_cls=00&cour_nm=SPECIAL TOPICS IN INTERNATIONAL LAW I(English)"}}, "EDBA231-02" {:kr {:professor "박철", :schedule ["월(9) 33-221" "\n목(1-2) 33-325"], :r true, :number "EDBA231-02", :name " 마케팅원론", :w false, :l true, :classification "전공필수 ", :credit-hours "3(  3)", :x true, :campus "세종", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0293&dept_cd=4633&cour_cd=EDBA231&cour_cls=02&cour_nm=마케팅원론"}, :en {:professor "Cheol Park", :schedule ["Mon(9) 33-221" "\nThu(1-2) 33-325"], :r true, :number "EDBA231-02", :name "PRINCIPLES OF MARKETING", :w false, :l true, :classification "Major Required", :credit-hours "3(  3)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0293&dept_cd=4633&cour_cd=EDBA231&cour_cls=02&cour_nm=PRINCIPLES OF MARKETING"}}, "KFBT253-00" {:kr {:professor "김영준", :schedule ["수(3-4) 7-217" "\n목(4) 9-615A"], :r true, :number "KFBT253-00", :name " 세포생물학", :w false, :l false, :classification "기본전공선택 ", :credit-hours "3(  3)", :x false, :campus "세종", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4460&dept_cd=4554&cour_cd=KFBT253&cour_cls=00&cour_nm=세포생물학"}, :en {:professor "Young Jun Kim", :schedule ["Wed(3-4) 7-217" "\nThu(4) 9-615A"], :r true, :number "KFBT253-00", :name "CELL BIOLOGY", :w false, :l false, :classification "Basic Major", :credit-hours "3(  3)", :x false, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4460&dept_cd=4554&cour_cd=KFBT253&cour_cls=00&cour_nm=CELL BIOLOGY"}}, "BUSS207-03" {:kr {:professor "김우찬", :schedule ["화(1) 현차관B204" "\n목(1) 현차관B204"], :r false, :number "BUSS207-03", :name " 재무관리(영강)", :w false, :l true, :classification "전공필수 ", :credit-hours "3(  3)", :x false, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0140&dept_cd=0142&cour_cd=BUSS207&cour_cls=03&cour_nm=재무관리(영강)"}, :en {:professor "Kim, Woochan", :schedule ["Tue(1) 151-B204" "\nThu(1) 151-B204"], :r false, :number "BUSS207-03", :name "FINANCIAL MANAGEMENT(English)", :w false, :l true, :classification "Major Required", :credit-hours "3(  3)", :x false, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0140&dept_cd=0142&cour_cd=BUSS207&cour_cls=03&cour_nm=FINANCIAL MANAGEMENT(English)"}}, "CHEM300-01" {:kr {:professor "이광렬", :schedule [], :r true, :number "CHEM300-01", :name " 전공심화연구I", :w false, :l false, :classification "전공선택 ", :credit-hours "3(  6)", :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0209&dept_cd=0213&cour_cd=CHEM300&cour_cls=01&cour_nm=전공심화연구I"}, :en {:professor "Kwangyeol Lee", :schedule [], :r true, :number "CHEM300-01", :name "UNDERGRADUATE ADVANCED RESEARCH I", :w false, :l false, :classification "Major Elective", :credit-hours "3(  6)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0209&dept_cd=0213&cour_cd=CHEM300&cour_cls=01&cour_nm=UNDERGRADUATE ADVANCED RESEARCH I"}}, "ELED241-00" {:kr {:professor "황지연", :schedule ["화(2) 사대신관 124" "\n목(2) 사대신관 124"], :r true, :number "ELED241-00", :name " 영미아동문학강독(영강)", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  3)", :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0234&dept_cd=0241&cour_cd=ELED241&cour_cls=00&cour_nm=영미아동문학강독(영강)"}, :en {:professor "Ji Yeon Hwang", :schedule ["Tue(2) 110-124" "\nThu(2) 110-124"], :r true, :number "ELED241-00", :name "READINGS IN ENGLISH CHILDREN'S LITERATURE(English)", :w false, :l true, :classification "Major Elective", :credit-hours "3(  3)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0234&dept_cd=0241&cour_cd=ELED241&cour_cls=00&cour_nm=READINGS IN ENGLISH CHILDREN'S LITERATURE(English)"}}, "ENGL421-03" {:kr {:professor "이종임", :schedule ["화(5) 교양관 402" "\n목(5) 교양관 402"], :r true, :number "ENGL421-03", :name " 영미문학특수과제Ⅰ(영강)", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  3)", :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0143&dept_cd=0146&cour_cd=ENGL421&cour_cls=03&cour_nm=영미문학특수과제Ⅰ(영강)"}, :en {:professor "Jong-Im Lee", :schedule ["Tue(5) 108-402" "\nThu(5) 108-402"], :r true, :number "ENGL421-03", :name "SPECIAL TOPICS IN BRITISH AND AMERICAN LITERATURE(English)", :w false, :l true, :classification "Major Elective", :credit-hours "3(  3)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0143&dept_cd=0146&cour_cd=ENGL421&cour_cls=03&cour_nm=SPECIAL TOPICS IN BRITISH AND AMERICAN LITERATURE(English)"}}, "COSE496-00" {:kr {:professor "한정현", :schedule [], :r false, :number "COSE496-00", :name " 현장실습II", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  0)", :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=5720&dept_cd=5722&cour_cd=COSE496&cour_cls=00&cour_nm=현장실습II"}, :en {:professor "HAN, JUNGHYUN", :schedule [], :r false, :number "COSE496-00", :name "INTERNSHIP PROGRAM II", :w false, :l true, :classification "Major Elective", :credit-hours "3(  0)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=5720&dept_cd=5722&cour_cd=COSE496&cour_cls=00&cour_nm=INTERNSHIP PROGRAM II"}}, "LSSS471-00" {:kr {:professor "배상우", :schedule ["수(8-9) 33-324"], :r false, :number "LSSS471-00", :name " 스포츠이벤트실습(영강)", :w false, :l true, :classification "전공선택 ", :credit-hours "1(  2)", :x false, :campus "세종", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=5685&dept_cd=5683&cour_cd=LSSS471&cour_cls=00&cour_nm=스포츠이벤트실습(영강)"}, :en {:professor "Sang Woo Bae", :schedule ["Wed(8-9) 33-324"], :r false, :number "LSSS471-00", :name "SPORT EVENT FIELDWORK(English)", :w false, :l true, :classification "Major Elective", :credit-hours "1(  2)", :x false, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=5685&dept_cd=5683&cour_cd=LSSS471&cour_cls=00&cour_nm=SPORT EVENT FIELDWORK(English)"}}, "KHSU230-00" {:kr {:professor "전옥희", :schedule ["금(1-2) 하나과학관 B117호"], :r true, :number "KHSU230-00", :name " 생화학", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  3)", :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4669&dept_cd=4893&cour_cd=KHSU230&cour_cls=00&cour_nm=생화학"}, :en {:professor "Ok-Hee Jeon", :schedule ["Fri(1-2) B117"], :r true, :number "KHSU230-00", :name "BIOCHEMISTRY", :w false, :l true, :classification "Major Elective", :credit-hours "3(  3)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4669&dept_cd=4893&cour_cd=KHSU230&cour_cls=00&cour_nm=BIOCHEMISTRY"}}})

(def dummy-processed {"DISS352-00" {:kr {:professor "이지수", :r false, :number "DISS352-00", :name " 국제법특강I(영강)", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  3)", :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=3928&dept_cd=3931&cour_cd=DISS352&cour_cls=00&cour_nm=국제법특강I(영강)"}, :en {:professor nil, :schedule [["Tue" 1 "138-322"] ["Thu" 1 "138-322"]], :r false, :number "DISS352-00", :name "SPECIAL TOPICS IN INTERNATIONAL LAW I(English)", :w false, :l true, :classification "Major Elective", :credit-hours "3(  3)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=3928&dept_cd=3931&cour_cd=DISS352&cour_cls=00&cour_nm=SPECIAL TOPICS IN INTERNATIONAL LAW I(English)"}}, "EDBA231-02" {:kr {:professor "박철", :r true, :number "EDBA231-02", :name " 마케팅원론", :w false, :l true, :classification "전공필수 ", :credit-hours "3(  3)", :x true, :campus "세종", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0293&dept_cd=4633&cour_cd=EDBA231&cour_cls=02&cour_nm=마케팅원론"}, :en {:professor "Cheol Park", :schedule [["Mon" 9 "33-221"] ["Thu" 1 "33-325"] ["Thu" 2 "33-325"]], :r true, :number "EDBA231-02", :name "PRINCIPLES OF MARKETING", :w false, :l true, :classification "Major Required", :credit-hours "3(  3)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0293&dept_cd=4633&cour_cd=EDBA231&cour_cls=02&cour_nm=PRINCIPLES OF MARKETING"}}, "KFBT253-00" {:kr {:professor "김영준", :r true, :number "KFBT253-00", :name " 세포생물학", :w false, :l false, :classification "기본전공선택 ", :credit-hours "3(  3)", :x false, :campus "세종", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4460&dept_cd=4554&cour_cd=KFBT253&cour_cls=00&cour_nm=세포생물학"}, :en {:professor "Young Jun Kim", :schedule [["Wed" 3 "7-217"] ["Wed" 4 "7-217"] ["Thu" 4 "9-615A"]], :r true, :number "KFBT253-00", :name "CELL BIOLOGY", :w false, :l false, :classification "Basic Major", :credit-hours "3(  3)", :x false, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4460&dept_cd=4554&cour_cd=KFBT253&cour_cls=00&cour_nm=CELL BIOLOGY"}}, "BUSS207-03" {:kr {:professor "김우찬", :r false, :number "BUSS207-03", :name " 재무관리(영강)", :w false, :l true, :classification "전공필수 ", :credit-hours "3(  3)", :x false, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0140&dept_cd=0142&cour_cd=BUSS207&cour_cls=03&cour_nm=재무관리(영강)"}, :en {:professor "Kim, Woochan", :schedule [["Tue" 1 "151-B204"] ["Thu" 1 "151-B204"]], :r false, :number "BUSS207-03", :name "FINANCIAL MANAGEMENT(English)", :w false, :l true, :classification "Major Required", :credit-hours "3(  3)", :x false, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0140&dept_cd=0142&cour_cd=BUSS207&cour_cls=03&cour_nm=FINANCIAL MANAGEMENT(English)"}}, "CHEM300-01" {:kr {:professor "이광렬", :r true, :number "CHEM300-01", :name " 전공심화연구I", :w false, :l false, :classification "전공선택 ", :credit-hours "3(  6)", :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0209&dept_cd=0213&cour_cd=CHEM300&cour_cls=01&cour_nm=전공심화연구I"}, :en {:professor "Kwangyeol Lee", :schedule [], :r true, :number "CHEM300-01", :name "UNDERGRADUATE ADVANCED RESEARCH I", :w false, :l false, :classification "Major Elective", :credit-hours "3(  6)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0209&dept_cd=0213&cour_cd=CHEM300&cour_cls=01&cour_nm=UNDERGRADUATE ADVANCED RESEARCH I"}}, "ELED241-00" {:kr {:professor "황지연", :r true, :number "ELED241-00", :name " 영미아동문학강독(영강)", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  3)", :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0234&dept_cd=0241&cour_cd=ELED241&cour_cls=00&cour_nm=영미아동문학강독(영강)"}, :en {:professor "Ji Yeon Hwang", :schedule [["Tue" 2 "110-124"] ["Thu" 2 "110-124"]], :r true, :number "ELED241-00", :name "READINGS IN ENGLISH CHILDREN'S LITERATURE(English)", :w false, :l true, :classification "Major Elective", :credit-hours "3(  3)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0234&dept_cd=0241&cour_cd=ELED241&cour_cls=00&cour_nm=READINGS IN ENGLISH CHILDREN'S LITERATURE(English)"}}, "ENGL421-03" {:kr {:professor "이종임", :r true, :number "ENGL421-03", :name " 영미문학특수과제Ⅰ(영강)", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  3)", :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0143&dept_cd=0146&cour_cd=ENGL421&cour_cls=03&cour_nm=영미문학특수과제Ⅰ(영강)"}, :en {:professor "Jong-Im Lee", :schedule [["Tue" 5 "108-402"] ["Thu" 5 "108-402"]], :r true, :number "ENGL421-03", :name "SPECIAL TOPICS IN BRITISH AND AMERICAN LITERATURE(English)", :w false, :l true, :classification "Major Elective", :credit-hours "3(  3)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=0143&dept_cd=0146&cour_cd=ENGL421&cour_cls=03&cour_nm=SPECIAL TOPICS IN BRITISH AND AMERICAN LITERATURE(English)"}}, "COSE496-00" {:kr {:professor "한정현", :r false, :number "COSE496-00", :name " 현장실습II", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  0)", :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=5720&dept_cd=5722&cour_cd=COSE496&cour_cls=00&cour_nm=현장실습II"}, :en {:professor "HAN, JUNGHYUN", :schedule [], :r false, :number "COSE496-00", :name "INTERNSHIP PROGRAM II", :w false, :l true, :classification "Major Elective", :credit-hours "3(  0)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=5720&dept_cd=5722&cour_cd=COSE496&cour_cls=00&cour_nm=INTERNSHIP PROGRAM II"}}, "LSSS471-00" {:kr {:professor "배상우", :r false, :number "LSSS471-00", :name " 스포츠이벤트실습(영강)", :w false, :l true, :classification "전공선택 ", :credit-hours "1(  2)", :x false, :campus "세종", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=5685&dept_cd=5683&cour_cd=LSSS471&cour_cls=00&cour_nm=스포츠이벤트실습(영강)"}, :en {:professor "Sang Woo Bae", :schedule [["Wed" 8 "33-324"] ["Wed" 9 "33-324"]], :r false, :number "LSSS471-00", :name "SPORT EVENT FIELDWORK(English)", :w false, :l true, :classification "Major Elective", :credit-hours "1(  2)", :x false, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=5685&dept_cd=5683&cour_cd=LSSS471&cour_cls=00&cour_nm=SPORT EVENT FIELDWORK(English)"}}, "KHSU230-00" {:kr {:professor "전옥희", :r true, :number "KHSU230-00", :name " 생화학", :w false, :l true, :classification "전공선택 ", :credit-hours "3(  3)", :x true, :campus "안암", :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanView.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4669&dept_cd=4893&cour_cd=KHSU230&cour_cls=00&cour_nm=생화학"}, :en {:professor "Ok-Hee Jeon", :schedule [["Fri" 1 "B117"] ["Fri" 2 "B117"]], :r true, :number "KHSU230-00", :name "BIOCHEMISTRY", :w false, :l true, :classification "Major Elective", :credit-hours "3(  3)", :x true, :href "http://infodepot.korea.ac.kr/lecture1/lecsubjectPlanViewEng.jsp?year=2015&term=1R&grad_cd=0136&col_cd=4669&dept_cd=4893&cour_cd=KHSU230&cour_cls=00&cour_nm=BIOCHEMISTRY"}}})

;; Utils
(defn course-hm->str
  [[_ data]]
  (str/join " "
            [(get-in data [:kr :name])
             (get-in data [:kr :number])
             (get-in data [:kr :professor])
             (apply str (get-in data [:kr :schedule]))
             (str/replace (get-in data [:kr :credit-hours]) " " "")
             (get-in data [:kr :classification])
             (get-in data [:en :name])
             (get-in data [:en :number])
             (get-in data [:en :professor])
             (apply str (get-in data [:kr :schedule]))
             (str/replace (get-in data [:en :credit-hours]) " " "")
             (get-in data [:en :classification])]))

(defn matches-query?
  [search-input course]
  (let [course-str (str/lower-case (course-hm->str course))
        search-input (str/lower-case search-input)
        search-tokens (str/split search-input #" ")]
    (every? (fn [search-token]
              (> (.indexOf course-str search-token) -1))
            search-tokens)))

;; Handlers (this will modify inner database)
(re-frame/register-handler
 :initialise-db
 (fn [_ _]
   {:courses dummy-data
    :processed-courses dummy-processed
    :search-input ""
    :my-courses []}))

(defn handle-search-input-entered
  [app-state [_ search-input]]
  (assoc-in app-state [:search-input] search-input))

(re-frame/register-handler
 :search-input-entered
 handle-search-input-entered)

(defn handle-course-search-entered
  [app-state _]
  (let [displayed-courses (re-frame/subscribe [:courses-to-display])
        search-elem (.getElementById js/document "search-courses")]
    (if (and (= 1 (count @displayed-courses))
             (not (get (set (get app-state :my-courses))
                       (first @displayed-courses))))
      (do (set! (.-value search-elem) "")
          (-> app-state
              (update-in [:my-courses]
                         conj
                         [(ffirst @displayed-courses)
                          (get-in (val (first @displayed-courses))
                                  [:kr :name])
                          (first (get-in (val (first @displayed-courses))
                                         [:kr :credit-hours]))])
              (assoc-in [:search-input] "")))
      app-state)))

(re-frame/register-handler
 :course-search-entered
 handle-course-search-entered)

;; Subscribers (this will get data based on inner database)

(re-frame/register-sub
 :search-input
 (fn [db]
   (reaction (:search-input @db))))

(re-frame/register-sub
 :search-tokens
 (fn [db]
   (reaction (str/split (str/lower-case (:search-input @db)) #" "))))

(re-frame/register-sub
 :courses
 (fn [db]
   (reaction (:courses @db))))

(re-frame/register-sub
 :my-courses
 (fn [db]
   (reaction (:my-courses @db))))

(re-frame/register-sub
 :not-my-courses
 (let [courses (re-frame/subscribe [:courses])
       my-courses (re-frame/subscribe [:my-courses])]
   (fn [db]
     (reaction (filterv (fn [[id course-data]]
                          (not (get (set (map first @my-courses)) id)))
                        @courses)))))

;; TODO: is it right pattern?
(re-frame/register-sub
 :filtered-courses
 (let [search-input (re-frame/subscribe [:search-input])
       not-my-courses (re-frame/subscribe [:not-my-courses])]
   (fn [_]
     (reaction
      (let [filtered-courses
            (filterv (partial matches-query? @search-input) @not-my-courses)]
        (if (and (empty? filtered-courses)
                 (> (count @search-input) 0))
          (filterv
           (partial matches-query? (apply str (butlast @search-input)))
           @not-my-courses)
          filtered-courses))))))

(re-frame/register-sub
 :courses-to-display
 (let [filtered-courses (re-frame/subscribe [:filtered-courses])]
   (fn [_]
     (reaction (take 5 @filtered-courses)))))

(re-frame/register-sub
 :count-courses-in-search
 (let [filtered (re-frame/subscribe [:filtered-courses])]
   (fn [_]
     (reaction (count @filtered)))))

(re-frame/register-sub
 :sum-credits-in-my-courses
 (let [my-courses (re-frame/subscribe [:my-courses])]
   (fn [_]
     (reaction (reduce + (mapv #(js/parseInt (nth % 2)) @my-courses))))))
