from django.views import generic

class IndexView(generic.TemplateView):
	template_name = 'login_test/index.html'

class SuccessView(generic.TemplateView):
	template_name = 'login_test/success.html'