; Kyle Cheng, kwc9ap, 11/5/20, binarySearch.s

	global binarySearch
	
	section .text 
	
; 1st parameter is pointer to a[0]
; 2nd parameter is integer representing beginning of array
; 3rd parameter is integer representing end of array
; 4th parameter is integer representing target element to find the array 

binarySearch:

	mov rax, -1	; rax = -1

start:
	cmp rsi, rdx	; if low > high
	jg done	; not found
	lea r10, [rsi+rdx]	; mid = (low+high)
	shr r10, 1		; mid /=2 
	cmp [rdi+4*r10], ecx	; if a[mid]
	jg greater		; > target go to greater
	jl less		; > target go to less
	mov rax, r10		; if a[mid] = target, rax = mid
done:
	ret
less:
	lea rsi, [r10+1]
	jmp start
greater:
	lea rdx, [r10-1]
	jmp start


	

