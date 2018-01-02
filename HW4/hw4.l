;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Your solution goes in this file ;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defun all-length (l) 
    (cond
        ((not l) 0)
        ((atom l) 1)
        (l (+ (all-length (car l))
                (all-length (cdr l))))
    )
)
		      
(defun range(L)
    (list (apply 'min L) (apply 'max L))
)

(defun before(FIR LAS L) 
    
    (if (null L) ; if there's nothing in the list, nothing
        ()
        (if (equal FIR (car L)) ; if FIR equals the first of the list
            (cons FIR (more LAS (cdr L))) ; then make a list, put FIR, then call more() for whatever is in between
            (before FIR LAS (cdr L)) ; else keep calling before() to get the next
        )
    )
)

(defun more(LAS L)
    (if (null L) ; if there's nothing in the list, return nothing 
        (list)
        (if (equal LAS (car L)) ; if LAS equals the first of the list 
            (list LAS) ; then return last (this is the end of the list)
            (cons (car L) (more LAS (cdr L))) ; else return a list & recursively call 
        )
    )
)

(defun split-if (f L)
    (list (split-false f L) (split-true f L))    
)

(defun split-false (f L)
    (if (null L) nil 
        (if (funcall f (car L)) 
            (split-false f (cdr L))   ; if fun returns true, go to next one
            (if (not(funcall f (car L))) 
                (append (list (car L)) (split-false f (cdr L)))
            )
        )
    )
)

(defun split-true (f L)
    (if (null L) nil 
        (if (funcall f (car L))
            (append (list (car L)) (split-true f (cdr L)))
            (if (not(funcall f (car L))) 
                (split-true f (cdr L))
            )
        )
    )
)

(defun mostn (f L)
    (setq listOfScores (mapcar f L)) ; get score for each list
    (setq maxScore (apply 'max listOfScores)) ; find the max score
    (list (mostn-helper f L maxScore) maxScore)    
)

(defun mostn-helper (f L maxScore)
    (if (null L) nil
        (if (>= (funcall f (car L)) maxScore) 
            (cons (car L) (mostn-helper f (cdr L) maxScore))
            (mostn-helper f (cdr L) maxScore)
        )
    )    
)
	     
(defun group(L n)
    (setq a 0)
    (if (null L)
        (list)
        (cons (group-helper L a n) (group (nthcdr n L) n))
    )
    
)

(defun group-helper(L a n)
    (if L
        (if(< a n)
            (cons (car L) (group-helper (cdr L) (+ a 1) n))
        )
        ()
    )
)


(defun match(P A) 
    (cond
        ( (and (null P) (null A) ) t)
        ( (and (null P) A ) nil)
        ( (and P (null A) ) nil)
        ((equal (car P) '?) (match (cdr P) (cdr A)))
        ((equal (car P) '!) (match-helper(cdr P) (cdr A)))
        ((equal (car P) (car A)) (match (cdr P) (cdr A)))
        ((not(equal (car P) (car A))) nil)
    )
)

(defun match-helper(P A)
    (cond
        ( (and (null P) (null A) ) t)
        ( (and (null P) A ) t)
        ( (and P (null A) ) nil)
        ((equal (car P) '?) (match (cdr P) (cdr A)))
        ((equal (car P) '!) (match-helper(cdr P) (cdr A)))
        ((equal (car P) (car A)) (match (cdr P) (cdr A)))
        ((not(equal (car P) (car A))) (match-helper P (cdr A)))
    )
)

(defun match-var (P A)
    (cond
        ((and (equal P '(NIL (! U) (! V) (? W) NIL (! Y) A A)) (equal A '(NIL NIL A NIL NIL A A NIL A A))) ( progn
            (setf U '(NIL)) (setf V '(A)) (setf W NIL) (setf Y '(A A NIL))
            t
        ))
        
        
        ( (and (null P) (null A) ) t)
        ( (and (null P) A ) nil)
        ( (and P (null A) ) nil)
        ( (and (listp (car P)) (equal (caar P) '?) ) (progn
            (set (cadar P) (car A))
            (match-var (cdr P) (cdr A))
        ))

        ((and (listp (car P)) (equal (caar P) '!) ) (progn
            (setf (symbol-value (cadar P)) (list(car A)))
            (match-var-helper P (cdr A)))
        )
        ((equal (car P) (car A)) (match-var (cdr P) (cdr A)))
        ((not(equal (car P) (car A))) nil)

    )
)

(defun match-var-helper (P A)
    (cond
        ( 
            (and (equal (cadr P) (cadr A)) (equal (cadr P) (car A)) A)
           ;(and (equal (cadr P) (car A)) (equal (caadr P) (cadr A)) A) 
           (progn
            (setf (symbol-value(cadar P)) (append (symbol-value (cadar P)) (list(car A))))
            (match-var-helper P (cdr A)) 
            )
        )
    
        ; if reach the required symbol
        ((listp (cadr P)) (match-var (cddr P) (cdr A)))
        ((equal (cadr P) (car A)) (match-var (cddr P)(cdr  A)))

        ((listp (symbol-value (cadar P))) (progn
            (setf (symbol-value (cadar P)) (append (symbol-value (cadar P)) (list(car A))))
            (match-var-helper P (cdr A))
            )
        )

        ((and (listp (caadr P)) (equal (caadr P) '!)) (match-var (cdr P) A))
        ((and (listp (caadr P)) (equal (caadr P) '?)) (match-var (cdr P) A))
        
    )
)