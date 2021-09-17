from django.test import TestCase

class TravisCITest(TestCase):
	def test_case_passes(self):
		"""
		Tests Travis-CI by passing
		"""
		self.assertEqual(True, True)