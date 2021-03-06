(import (igropyr http))

(printf "server is start, listen on port..~a\n" 8080)

(define get
        (lambda (request_header pathinfo query_string)
            (response 200 "text/html"
                (string-append "<p>path is:" pathinfo "</br>query is:" (if query_string query_string "nothing"))))))
                
(define post
        (lambda (request_header pathinfo payload)
            (response 200 "application/json" "{\"hello\":\"world\"}"))))

(server 
    (request get)
    (request post)
    (set)
    (listen))



