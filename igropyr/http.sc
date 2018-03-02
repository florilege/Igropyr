(library (igropyr http)
  (export
    server
    listen
    set
    request
    response
    par
  )
  (import
    (scheme)
  )




  (define lib (load-shared-object "./lib/igropyr/httpc.so"))

  (define igropyr_init
    (foreign-procedure "igropyr_init" (string string int) int)
  )

  (define handle_request
    (foreign-procedure "handle_request" (iptr iptr) int))

  (define response
    (foreign-procedure "igropyr_response" (int string string) string))

  (define par
    (foreign-procedure "igropyr_par" (string string) boolean))


  (define request
    (lambda (info)
        (let ((code (foreign-callable info (string string string) string)))
            (lock-object code)
            (foreign-callable-entry-point code))))

  (define ref
    (lambda (str x)
      (if (null? str)
        '()
        (if (equal? (caar str) x)
          (cdar str)
          (ref (cdr str) x)))))

  (define-syntax set
    (lambda (x)
      (syntax-case x ()
        ((_) #''())
        ((_ (e1 e2)) #'(list (cons e1 e2))))))

  (define-syntax listen
    (lambda (x)
      (syntax-case x ()
        ((_) #''())
        ((_ e) #'(cond 
                  ((string? e) (list (cons 'ip e)))
                  ((integer? e) (list (cons 'port e)))
                  (else '())))
        ((_ e1 e2) #'(list (cons 'ip e1)(cons 'port e2))))))

  (define server 
    (lambda (req_get req_post set listen)
      (let ((staticpath (ref set 'staticpath))
            ;(connections (ref set 'connections))
            ;(keepalive (ref set 'keepalive))
            (ip (ref listen 'ip))
            (port (ref listen 'port)))
        (begin 
          (handle_request req_get req_post)
          (igropyr_init
            (if (null? staticpath)
            ""
            staticpath)
        ;(if (null? connections)
        ;   1024
        ;   connections)
        ;(if (null? keepalive)
        ;   5000
        ;   keepalive)
          (if (null? ip)
            "0.0.0.0"
            ip)
          (if (null? port)
            80
            port))))))
  
)

