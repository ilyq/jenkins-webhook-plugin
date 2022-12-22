package io.jenkins.plugins.sample;

import hudson.Launcher;
import hudson.EnvVars;
import hudson.Extension;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.tasks.BuildStepDescriptor;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.kohsuke.stapler.DataBoundSetter;

public class HelloWorldBuilder extends Notifier {

	private final String name;
	private boolean useFrench;

	@DataBoundConstructor
	public HelloWorldBuilder(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isUseFrench() {
		return useFrench;
	}

	@DataBoundSetter
	public void setUseFrench(boolean useFrench) {
		this.useFrench = useFrench;
	}

	@Override
	public boolean prebuild(AbstractBuild<?, ?> build, BuildListener listener) {
		// return super.prebuild(build, listener);

		try {
			// 获取jenkins环境变量
			final EnvVars env = build.getEnvironment(listener);
			// listener.getLogger().println(env);

			// 构建环境变量payload
			Map<String, String> payloadObj = new HashMap<>();
			env.forEach((key, val) -> payloadObj.put(key, val));
			Gson gson = new Gson();
			String payload = gson.toJson(payloadObj);
			// listener.getLogger().println(payload);

			// 发送请求
			CloseableHttpClient client = HttpClients.createDefault();
			try {
				HttpPost httpPost = new HttpPost(name);

				StringEntity entity = new StringEntity(payload);
				httpPost.setEntity(entity);
				httpPost.setHeader("Content-type", "application/json");

				CloseableHttpResponse response = client.execute(httpPost);
				listener.getLogger().println("status code: " + response.getStatusLine().getStatusCode());

				HttpEntity responseEntity = response.getEntity();
				listener.getLogger().println("response data: " + EntityUtils.toString(responseEntity));

			} catch (Exception e) {
				listener.getLogger().println(e);
			} finally {
				client.close();
			}

		} catch (Exception e) {
			// throw new RuntimeException(e);
			listener.getLogger().println(e);
		}

		return true;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
			throws InterruptedException, IOException {

		try {
			// 获取jenkins环境变量
			final EnvVars env = build.getEnvironment(listener);
			// listener.getLogger().println(env);

			// 构建环境变量payload
			Map<String, String> payloadObj = new HashMap<>();
			env.forEach((key, val) -> payloadObj.put(key, val));
			Gson gson = new Gson();
			String payload = gson.toJson(payloadObj);
			// listener.getLogger().println(payload);

			// 发送请求
			CloseableHttpClient client = HttpClients.createDefault();
			try {
				HttpPost httpPost = new HttpPost(name);

				StringEntity entity = new StringEntity(payload);
				httpPost.setEntity(entity);
				httpPost.setHeader("Content-type", "application/json");

				CloseableHttpResponse response = client.execute(httpPost);
				listener.getLogger().println("status code: " + response.getStatusLine().getStatusCode());

				HttpEntity responseEntity = response.getEntity();
				listener.getLogger().println("response data: " + EntityUtils.toString(responseEntity));

			} catch (Exception e) {
				listener.getLogger().println(e);
			} finally {
				client.close();
			}

		} catch (Exception e) {
			// throw new RuntimeException(e);
			listener.getLogger().println(e);
		}

		return true;
	}

	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	@Extension
	public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

		public FormValidation doCheckName(@QueryParameter String value, @QueryParameter boolean useFrench)
				throws IOException, ServletException {
			if (value.length() == 0)
				return FormValidation.error(Messages.HelloWorldBuilder_DescriptorImpl_errors_missingName());
			if (value.length() < 4)
				return FormValidation.warning(Messages.HelloWorldBuilder_DescriptorImpl_warnings_tooShort());
			if (!useFrench && value.matches(".*[éáàç].*")) {
				return FormValidation.warning(Messages.HelloWorldBuilder_DescriptorImpl_warnings_reallyFrench());
			}
			return FormValidation.ok();
		}

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			return true;
		}

		@Override
		public String getDisplayName() {
			// return Messages.HelloWorldBuilder_DescriptorImpl_DisplayName();
			return "webhook";
		}

	}

}
